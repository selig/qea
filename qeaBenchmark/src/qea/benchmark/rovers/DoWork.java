package qea.benchmark.rovers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public abstract class DoWork<S> {

	public abstract void run_with_spec(S spec, String name, int[] args);

	public void dowork(String name, int[] args) {
		if (name.equals("IncreasingCommand")) {
			work_for_IncreasingCommand(args[0]);
		} else if (name.equals("ResourceLifecycle")) {
			work_for_ResourceLifecycle(args[0], args[1]);
		} else if (name.equals("ExactlyOneSuccess")) {
			work_for_ExactlyOneSuccess(args[0]);
		} else if (name.equals("AcknowledgeCommands")) {
			work_for_AcknowledgeCommands(args[0]);
		} else if (name.equals("NestedCommand")) {
			work_for_NestedCommand(args[0], args[1], args[2]);
		} else if (name.equals("GrantCancel")) {
			work_for_GrantCancel(args[0], args[1], args[2]);
		} else if (name.equals("ReleaseResource")) {
			work_for_ReleaseResource(args[0], args[1], args[2]);
		} else if (name.equals("RespectConflicts")) {
			work_for_RespectConflicts(args[0], args[1], args[2]);
		} else if (name.equals("ExistsSatellite")) {
			work_for_ExistsSatellite(args[0], args[1]);
		} else if (name.equals("ExistsLeader")) {
			work_for_ExistsLeader(args[0]);
		} else if (name.equals("MessageHashCorrectInvInt")) {
			work_for_MessageHashCorrectInvInt(args[0], args[1]);
		} else if (name.equals("RespectPriorities")) {
			work_for_RespectPriorities(args[0], args[1]);
		} else {
			System.err.println(name + " not found");
		}
	}

	/**
	 * Generates events for the IncreasingCommand property with the specified
	 * parameters
	 * 
	 * @param c
	 *            Number of identifiers
	 */
	public void work_for_IncreasingCommand(int c) {

		for (int i = 0; i < c; i++) {
			command_int(i);
		}

	}

	/**
	 * Generates events for the ResourceLifecycle property with the specified
	 * parameters
	 * 
	 * @param r
	 *            Number of resources
	 * @param u
	 *            Number of events to generate
	 */
	public void work_for_ResourceLifecycle(int r, int u) {

		// Initialise resources array
		Object[] ros = new Object[r];
		for (int i = 0; i < r; i++) {
			ros[i] = new Object();
		}

		int[] state = new int[r];

		// Currently seems necessary to make MOP work... explore this more
		// after paper is done!!
		for (int i = 0; i < ros.length; i++) {
			request(ros[i]);
			deny(ros[i]);
		}

		Random rand = new Random();
		for (int i = 0; i < u; i++) {

			// Choose a resource randomly
			int res = rand.nextInt(r);
			Object resource = ros[res];

			// Get current state
			int s = state[res];

			// Initialise next state
			int sp = -1;

			// According to the state, generate event
			switch (s) {
			case 0:
				request(resource);
				sp = 1;
				break;
			case 1:
				if (rand.nextBoolean()) {
					grant_rl(resource);
					sp = 2;
				} else {
					deny(resource);
					sp = 0;
				}
				;
				break;
			case 2:
				if (rand.nextBoolean()) {
					rescind(resource);
					sp = 2;
				} else {
					cancel_rl(resource);
					sp = 0;
				}
				;
				break;
			}

			// Update state
			state[res] = sp;
		}

	}

	public void work_for_ExactlyOneSuccess(int c) {

		System.err.println("Warning: cannot be used for MOP currently");

		Object[] cos = new Object[c];
		for (int i = 0; i < c; i++) {
			cos[i] = new Object();
		}
		Queue<Integer> to_suc = new LinkedList<Integer>();
		int c_made = 0;

		Random rand = new Random();
		while (c_made < c || !to_suc.isEmpty()) {

			if (c_made == c || !to_suc.isEmpty() && rand.nextBoolean()) {
				int com = to_suc.remove();
				succeed(cos[com]);
			} else {
				to_suc.add(c_made);
				command(cos[c_made]);
				c_made++;
			}
		}
	}

	private class AC_tosuc_Entry {
		public final int com;
		public final int time;
		public final Object node;
		public final Object payload;

		public AC_tosuc_Entry(int a, int b, Object c, Object d) {
			com = a;
			time = b;
			node = c;
			payload = d;
		}
	}

	public void work_for_AcknowledgeCommands(int c) {

		Object[] cos = new Object[c];
		for (int i = 0; i < c; i++) {
			cos[i] = new Object();
		}
		Set<Object> extra_c = new HashSet<Object>();
		set_ack_timeout(100);

		Queue<AC_tosuc_Entry> to_suc = new LinkedList<AC_tosuc_Entry>(); // Set[(Int,Object,Object,Int)]
																			// =
																			// Set()

		int c_made = 0;
		int clock = 0;

		Random rand = new Random();
		while (c_made < c || !to_suc.isEmpty()) {
			if (c_made == c || !to_suc.isEmpty() && rand.nextBoolean()) {
				AC_tosuc_Entry entry = to_suc.remove(); // (com,n,p,t) =
														// to_suc.head
				if (rand.nextBoolean()) {
					Object new_c = new Object();
					extra_c.add(new_c);
					command(new_c, entry.node, entry.payload, entry.time + 150);
				} else {
					ack(cos[entry.com], entry.time + 50);
				}
			} else {
				Object node = new Object();
				Object payload = new Object();
				AC_tosuc_Entry entry = new AC_tosuc_Entry(c_made, clock, node,
						payload);
				to_suc.add(entry);
				command(cos[c_made], node, payload, clock);
				clock++;
				c_made++;

			}
		}
	}

	private void nested_do_rep(int d, int breadth) {
		if (d > 0) {
			for (int b = 0; b < breadth; b++) {
				Object c = new Object();
				command(c);
				nested_do_rep(d - 1, breadth);
				succeed(c);
			}
		}
	}

	public void work_for_NestedCommand(int depth, int breadth, int reps) {
		for (int i = 0; i < reps; i++) {
			nested_do_rep(depth, breadth);
		}
	}

	/**
	 * Generates events for the GrantCancel property with the specified
	 * parameters.
	 * 
	 * @param t
	 *            Number of tasks
	 * @param r
	 *            Number of resources
	 * @param u
	 *            Number of events to generate
	 */
	public void work_for_GrantCancel(int t, int r, int u) {

		// Initialise tasks
		Object[] tos = new Object[t];
		for (int i = 0; i < t; i++) {
			tos[i] = new Object();
		}

		// Initialise resources
		Object[] ros = new Object[r];
		for (int i = 0; i < r; i++) {
			ros[i] = new Object();
		}

		// Initialise owners (tasks) for each resource. -1 means not owned
		int[] owning = new int[r];
		for (int i = 0; i < r; i++) {
			owning[i] = -1;
		}

		Random rand = new Random();
		for (int i = 0; i < u; i++) {

			// Choose a resource randomly
			int res = rand.nextInt(r);

			int task = owning[res];
			if (task == -1) { // No task owns the resource

				// Choose a task randomly and grant the resource
				task = rand.nextInt(t);
				grant_gc(tos[task], ros[res]);
				owning[res] = task;

			} else { // Some task owns the resource

				// Release the resource
				cancel_gc(tos[task], ros[res]);
				owning[res] = -1;
			}
		}
	}

	public void work_for_ReleaseResource(int t, int r, int e) {

		Object[] tos = new Object[t];
		for (int i = 0; i < t; i++) {
			tos[i] = new Object();
		}
		Object[] ros = new Object[r];
		for (int i = 0; i < r; i++) {
			ros[i] = new Object();
		}

		Map<Integer, Integer> t_to_c = new HashMap<Integer, Integer>();
		for (int i = 0; i <= t; i++) {
			t_to_c.put(i, -1);
		}

		Map<Integer, Queue<Integer>> t_to_r = new HashMap<Integer, Queue<Integer>>();
		for (int i = 0; i <= t; i++) {
			t_to_r.put(i, new LinkedList<Integer>());
		}

		int[] resources = new int[r];
		for (int i = 0; i < r; i++) {
			resources[i] = -1;
		}

		int r_used = 0;
		int com_used = 0;
		Map<Integer, Object> cos = new HashMap<Integer, Object>();

		Random rand = new Random();
		for (int i = 0; i < e; i++) {

			int task = rand.nextInt(t);
			int com = t_to_c.get(task);
			if (com == -1) {
				// schedule new command
				Object com_used_obj = new Object();
				schedule(tos[task], com_used_obj);
				cos.put(com_used, com_used_obj);
				t_to_c.put(task, com_used);
				com_used++;
			} else {
				Queue<Integer> res = t_to_r.get(task);
				if (r_used == r && res.isEmpty()) {
					// complete task
					finish(cos.get(com));
					t_to_c.put(task, -1);
				} else {
					if (r_used == r || !res.isEmpty() && rand.nextBoolean()) {
						// remove resource
						int rr = res.remove();
						resources[rr] = -1;
						cancel_rr(tos[task], ros[rr]);
						r_used--;
					} else {
						if (res.isEmpty() && rand.nextBoolean()) {
							// complete task
							finish(cos.get(com));
							t_to_c.put(task, -1);
						} else {
							// take a new resource
							int rr = 0;
							while (resources[rr] != -1) {
								rr += 1;
							}
							r_used++;
							resources[rr] = task;
							grant_rr(tos[task], ros[rr]);
							res.add(rr);
						}
					}
				}
			}
		}
	}

	public void work_for_RespectConflicts(int r, int s, int t) {

		// groups of things that are not in conflict with each other
		List<List<Object>> groups = new ArrayList<List<Object>>();

		for (int i = 0; i < r; i++) {
			List<Object> group = new ArrayList<Object>();
			for (int j = 0; j < r; j++) {
				final String label = i + "_" + j;
				group.add(new Object() {
					@Override
					public String toString() {
						return label;
					}
				});
			}
			groups.add(group);
		}

		Random rand = new Random();

		// create conflicts between groups
		for (int i = 0; i < r; i++) {
			List<Object> group_one = groups.get(i);
			for (int j = 0; j < r; j++) {
				if (i != j) {
					List<Object> group_two = groups.get(j);
					for (int k = 0; k < r; k++) {
						for (int l = 0; l < r; l++) {
							conflict(group_one.get(k), group_two.get(l));
						}
					}
				}
			}
		}

		// do work
		for (int i = 0; i < s; i++) {
			// println("s:"+i)
			// pick a group
			int group_id = rand.nextInt(r);
			List<Object> group = groups.get(group_id);
			boolean[] granted = new boolean[r];
			// pick t resources and either grant or release them
			for (int j = 0; j < t; j++) {
				int res_id = rand.nextInt(r);
				if (granted[res_id]) {
					cancel_rc(group.get(res_id));
				} else {
					grant_rc(group.get(res_id));
				}
				granted[res_id] = !granted[res_id];
			}
			// release any still granted resources
			for (int j = 0; j < r; j++) {
				if (granted[j]) {
					cancel_rc(group.get(j));
				}
			}
		}
	}

	public void work_for_ExistsSatellite(int s, int r) {

		Object[] sos = new Object[s];
		for (int i = 0; i < s; i++) {
			sos[i] = new Object();
		}
		Object[] ros = new Object[r];
		for (int i = 0; i < r; i++) {
			ros[i] = new Object();
		}

		Random rand = new Random();
		// generate map from satellites to rovers
		Map<Integer, Queue<Object>> map_p = new HashMap<Integer, Queue<Object>>();
		for (int sat = 0; sat < s; sat++) {
			Queue<Object> res = new LinkedList<Object>();
			for (int i = 0; i < r; i++) {
				if (rand.nextBoolean()) {
					res.add(ros[i]);
				}
			}
			map_p.put(sat, res);
		}
		Map<Integer, Queue<Object>> map_r = new HashMap<Integer, Queue<Object>>();

		List<Integer> sats_p = new ArrayList<Integer>();
		for (int i = 0; i < s; i++) {
			sats_p.add(i);
		}
		List<Integer> sats_r = new ArrayList<Integer>();

		while (!sats_p.isEmpty() || !sats_r.isEmpty()) {

			if (!sats_p.isEmpty() && rand.nextBoolean() || sats_r.isEmpty()) {

				// important that it's an object for list remove method
				Integer sat = sats_p.get(rand.nextInt(sats_p.size()));

				Queue<Object> res = map_p.get(sat);
				Object rr = res.remove();

				ping(rr, sos[sat]);

				Queue<Object> temp_q = map_r.get(sat);
				if (temp_q == null) {
					sats_r.add(sat);
					temp_q = new LinkedList();
					map_r.put(sat, temp_q);
				}
				temp_q.add(rr);

				if (res.peek() == null) {
					map_p.remove(sat);
					sats_p.remove(sat);
				}
			} else {
				// important that it's an object for list remove method
				Integer sat = sats_r.get(rand.nextInt(sats_r.size()));
				Queue<Object> res = map_r.get(sat);
				Object rr = res.remove();
				ack(sos[sat], rr);

				if (res.peek() == null) {
					map_r.remove(sat);
					sats_r.remove(sat);
				}
			}
		}
	}

	public void work_for_ExistsLeader(int r) {

		// translate resource ids into objects
		Object[] ros = new Object[r];
		for (int i = 0; i < r; i++) {
			ros[i] = new Object();
		}

		Random rand = new Random();

		// maps resources into the the resources it should ping
		Map<Integer, Queue<Integer>> map_ping = new HashMap<Integer, Queue<Integer>>();

		// randomly selects those resources
		for (int res = 0; res < r; res++) {
			Queue<Integer> o_res = new LinkedList<Integer>();
			for (int i = 0; i < r; i++) {
				if (i!=res && rand.nextBoolean()) {
					o_res.add(i);
				}
			}
			if (!o_res.isEmpty()) {
				map_ping.put(res, o_res);
			}
		}

		// pick a leader and set to full ping
		int leader = rand.nextInt(r);
		Queue<Integer> leader_res = new LinkedList<Integer>();
		for (int i = 0; i < r; i++) {
			if(i!=leader)
				leader_res.add(i);
		}
		System.err.println("Leader is "+ System.identityHashCode(ros[leader]));
		map_ping.put(leader, leader_res);

		// maps resources into the resources it should acknowledge
		Map<Integer, Queue<Integer>> map_ack = new HashMap<Integer, Queue<Integer>>();

		// set to true if some pings left
		boolean[] res_p = new boolean[r];
		int res_p_left = 0;
		for (int i : map_ping.keySet()) {
			res_p[i] = true;
			res_p_left++;
		}

		// record acks to go
		int res_a_left = 0;
		boolean[] res_a = new boolean[r];

		// while we have pings and acks left
		while (res_p_left > 0 || res_a_left > 0) {

			// System.err.println(res_p_left+","+res_a_left);

			// if we decide to ping
			if (res_p_left > 0 && rand.nextBoolean() || res_a_left == 0) {

				// pick a random res to send res_p
				int res = rand.nextInt(r);
				while (!res_p[res]) {
					res = rand.nextInt(r);
				}

				Queue<Integer> resq = map_ping.get(res);
				// select a res to receive
				int rr = resq.remove();

				ping(ros[res], ros[rr]);

				// rr now needs to do another ack
				if (!res_a[rr]) {
					res_a_left++;
					res_a[rr] = true;
				}
				// add res to the ack resources of rr
				Queue<Integer> oldq = map_ack.get(rr);
				if (oldq == null) {
					oldq = new LinkedList<Integer>();
					map_ack.put(rr, oldq);
				}
				oldq.add(res);

				if (resq.isEmpty()) {
					map_ping.remove(res);
					res_p[res] = false;
					res_p_left--;
				}

			} else { // send an ack instead

				int res = rand.nextInt(r);
				while (!res_a[res]) {
					res = rand.nextInt(r);
				}
				Queue<Integer> resq = map_ack.get(res);

				int rr = resq.remove();

				ack(ros[res], ros[rr]);

				if (resq.isEmpty()) {
					map_ack.remove(res);
					res_a[res] = false;
					res_a_left--;
				}

			}

		}

	}

	private static class mhc_Entry {
		public final Object sender;
		public final Object receiver;
		public final int hash;

		public mhc_Entry(Object a, Object b, int c) {
			sender = a;
			receiver = b;
			hash = c;
		}
	}

	public void work_for_MessageHashCorrectInvInt(int x, int ms) {

		Object[] xos = new Object[x];
		for (int i = 0; i < x; i++) {
			xos[i] = new Object();
		}

		Queue<mhc_Entry> to_ack = new LinkedList<mhc_Entry>();

		int sent = 0;
		int send_max = ms / 2;

		Random rand = new Random();

		while (sent < send_max || !to_ack.isEmpty()) {

			if (sent < send_max && (to_ack.isEmpty() || rand.nextBoolean())) {
				// send a new message
				// pick sender
				Object sender = xos[rand.nextInt(x)];
				Object receiver = sender;
				while (receiver == sender) {
					receiver = xos[rand.nextInt(x)];
				}
				int msg = rand.nextInt();
				int h = -msg;
				to_ack.add(new mhc_Entry(sender, receiver, h));
				send(sender, receiver, msg);
				sent++;
			} else {
				// ack a message
				mhc_Entry entry = to_ack.remove();
				ack(entry.receiver, entry.sender, entry.hash);

			}
			// println("=============================================")
			// m.asInstanceOf[MonitorConfig].getConfigs foreach println
		}

	}

	private void make_res(Map<Object, Set<Object>> pmap,
			List<Object> resources, int r, Set<Object> parents) {

		if (resources.size() >= r) {
			return;
		}

		Set<Object> this_level = new HashSet<>();
		for (Object pr : parents) {
			Set<Object> above_pr = new HashSet<>();
			for (Map.Entry<Object, Set<Object>> entry : pmap.entrySet()) {
				if (entry.getValue().contains(pr)) {
					above_pr.add(entry.getKey());
				}
			}
			if (resources.size() < r) {
				Object c1 = new Object();
				this_level.add(c1);
				resources.add(c1);
				for (Object apr : above_pr) {
					Set<Object> news = new HashSet<>(pmap.get(apr));
					news.add(c1);
					pmap.put(apr, news);
				}
			}
			// Do it again, i.e. binary tree
			if (resources.size() < r) {
				Object c1 = new Object();
				this_level.add(c1);
				resources.add(c1);
				for (Object apr : above_pr) {
					Set<Object> news = new HashSet<>(pmap.get(apr));
					news.add(c1);
					pmap.put(apr, news);
				}
			}
		}
		make_res(pmap, resources, r, this_level);
	}

	public void work_for_RespectPriorities(int r, int e) {
		// first create a tree-structure of r resources with priorities
		Map<Object, Set<Object>> pmap = new HashMap<>();

		List<Object> resources = new ArrayList<>();

		Object mom = new Object();
		resources.add(mom);
		Set<Object> moms = new HashSet<Object>();
		moms.add(mom);
		make_res(pmap, resources, r, moms);

		// submit those priorities to the monitor
		int e_done = 0;
		for (Map.Entry<Object, Set<Object>> entry : pmap.entrySet()) {
			Object r1 = entry.getKey();
			Set<Object> rs = entry.getValue();
			for (Object r2 : rs) {
				priority(r1, r2);
				e_done++;
			}
		}

		if (e_done > e) {
			throw new RuntimeException(
					"The number of events must be enough to allow the priorities to be given");
		}
		// else System.err.println(e-e_done+" events left");

		// now for the rest of the events select a random resource and do
		// something with it, randomly
		Random rand = new Random();

		Map<Object, Integer> rev = new HashMap<>();
		for (int i = 0; i < resources.size(); i++) {
			rev.put(resources.get(i), i);
		}
		int[] status = new int[r + 1];
		// statuses
		// 0 unrequested
		// 1 requested
		// 2 granted

		for (int i = e_done; i < e; i++) {
			int index = rand.nextInt(r);
			Object res = resources.get(index);
			switch (status[index]) {

			case 0:
				request(res);
				status[index] = 1;
				break;
			// if requested we can either grant or deny, if we plan on granting
			// it we need to make sure
			// that all conflicted resources with lower priority are rescinded
			// we can only grant if there is no resource with higher priority
			// granted
			case 1:
				boolean higher_g = false;
				for (Map.Entry<Object, Set<Object>> entry : pmap.entrySet()) {
					if (entry.getValue().contains(res)) {
						if (status[rev.get(entry.getKey())] == 2) {
							higher_g = true;
						}
					}
				}
				if (higher_g) {
					deny(res);
					status[index] = 0;
				} else {
					// let's try and grant it
					Set<Object> res_set = pmap.get(res);
					if (res_set != null) {
						for (Object lr : res_set) {
							if (status[rev.get(lr)] == 2) {
								rescind(lr);
								cancel_rp(lr);
								status[rev.get(lr)] = 0;
							}
						}
					}
					grant_rp(res);
					status[index] = 2;
				}
				break;
			case 2:
				cancel_rp(res);
				status[index] = 0;
			}
		}

	}

	/*
	 * The methods that we override to call the monitor. Events of the
	 * properties
	 */

	// not used?
	public abstract void command_int(int x);

	public abstract void request(Object o);

	public abstract void grant_rl(Object o);

	public abstract void grant_rc(Object o);

	public abstract void grant_rp(Object o);

	public abstract void deny(Object o);

	public abstract void rescind(Object o);

	public abstract void cancel_rl(Object o);

	public abstract void cancel_rc(Object o);

	public abstract void cancel_rp(Object o);

	public abstract void succeed(Object o);

	public abstract void command(Object o);

	public abstract void set_ack_timeout(int x);

	public abstract void command(Object a, Object b, Object c, int d);

	// not used?
	public abstract void ack(Object o, int x);

	public abstract void grant_gc(Object a, Object b);

	public abstract void grant_rr(Object a, Object b);

	public abstract void cancel_gc(Object a, Object b);

	public abstract void cancel_rr(Object a, Object b);

	public abstract void schedule(Object a, Object b);

	public abstract void finish(Object o);

	public abstract void conflict(Object a, Object b);

	public abstract void ping(Object a, Object b);

	public abstract void ack(Object a, Object b);

	public abstract void ack(Object a, Object b, int c);

	public abstract void send(Object a, Object b, int c);

	public abstract void fail(Object o);

	public abstract void priority(Object a, Object b);

}
