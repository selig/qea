package benchmark.rovers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

abstract class DoWork<S> {

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
		}

	}

	public void work_for_IncreasingCommand(int c) {

		for (int i = 0; i < c; i++) {
			command(i);
		}

	}

	public void work_for_ResourceLifecycle(int r, int u) {

		Object[] ros = new Object[r];
		for (int i = 0; i < r; i++) {
			ros[i] = new Object();
		}
		int[] resources = new int[r];

		Random rand = new Random();
		for (int i = 0; i < u; i++) {
			int res = rand.nextInt(r);
			int s = resources[res];
			int sp = -1;
			switch (s) {
			case 0:
				request(ros[res]);
				sp = 1;
				break;
			case 1:
				if (rand.nextBoolean()) {
					grant(ros[res]);
					sp = 2;
				} else {
					deny(ros[res]);
					sp = 0;
				}
				;
				break;
			case 2:
				if (rand.nextBoolean()) {
					rescind(ros[res]);
					sp = 2;
				} else {
					cancel(ros[res]);
					sp = 0;
				}
				;
				break;
			}
			resources[res] = sp;
		}

	}

	public void work_for_ExactlyOneSuccess(int c) {

		Object[] cos = new Object[c];
		for (int i = 0; i < c; i++) {
			cos[i] = new Object();
		}
		Queue<Integer> to_suc = new LinkedList<Integer>();
		int c_made = 0;

		Random rand = new Random();
		while (c_made < c || !to_suc.isEmpty()) {

			if (c_made == c || (!to_suc.isEmpty() && rand.nextBoolean())) {
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
			if (c_made == c || (!to_suc.isEmpty() && rand.nextBoolean())) {
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
				grant(tos[task], ros[res]);
				owning[res] = task;

			} else { // Some task owns the resource

				// Release the resource
				cancel(tos[task], ros[res]);
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
					if (r_used == r || (!res.isEmpty() && rand.nextBoolean())) {
						// remove resource
						int rr = res.remove();
						resources[rr] = -1;
						cancel(tos[task], ros[rr]);
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
							grant(tos[task], ros[rr]);
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
					cancel(group.get(res_id));
				} else {
					grant(group.get(res_id));
				}
				granted[res_id] = !granted[res_id];
			}
			// release any still granted resources
			for (int j = 0; j < r; j++) {
				if (granted[j]) {
					cancel(group.get(j));
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

			if ((!sats_p.isEmpty() && rand.nextBoolean()) || sats_r.isEmpty()) {

				// important that it's an object for list remove method
				Integer sat = sats_p.get(rand.nextInt(sats_p.size()));

				Queue<Object> res = map_p.get(sat);
				Object rr = res.remove();

				ping(rr, sos[sat]);
				sats_r.add(sat);
				Queue<Object> temp_q = map_r.get(sat);
				if (temp_q == null) {
					temp_q = new LinkedList();
					temp_q.add(rr);
				}
				map_r.put(sat, temp_q);

				if (res.size() == 1) {
					map_p.remove(sat);
					sats_p.remove(sat);
				}
			} else {
				Integer sat = sats_r.get(rand.nextInt(sats_r.size())); // important
																		// that
																		// it's
																		// an
																		// object
																		// for
																		// list
																		// remove
																		// method
				Queue<Object> res = map_r.get(sat);
				Object rr = res.remove();
				ack(sos[sat], rr);

				if (res.size() == 1) {
					map_r.remove(sat);
					sats_r.remove(sat);
				}
			}
		}

		//

	}

	public static void work_for_ExistsLeader(int r) {

		Object[] ros = new Object[r];
		for (int i = 0; i < r; i++) {
			ros[i] = new Object();
		}

		Random rand = new Random();

		Map<Integer, Queue<Integer>> map_p = new HashMap<Integer, Queue<Integer>>();
		for (int res = 0; res < r; res++) {
			Queue<Integer> o_res = new LinkedList<Integer>();
			for (int i = 0; i < r; i++) {
				if (rand.nextBoolean()) {
					o_res.add(i);
				}
			}
			if (!o_res.isEmpty()) {
				map_p.put(res, o_res);
			}
		}

		int leader = rand.nextInt(r);
		Queue<Integer> o_res = new LinkedList<Integer>();
		for (int i = 0; i < r; i++) {
			o_res.add(i);
		}
		map_p.put(leader, o_res);

		Map<Integer, Queue<Integer>> map_a = new HashMap<Integer, Queue<Integer>>();

		List<Integer> res_p = new ArrayList<Integer>();
		for (int i : map_p.keySet()) {
			res_p.add(i);
		}

		List<Integer> res_a = new ArrayList<Integer>();

		System.err
				.println("I got part way through ExistsLeader then gave up translating as we probs won't use it");
		/*
		 * while(!res_p.isEmpty() || !res_a.isEmpty()){
		 * 
		 * if((!res_p.isEmpty() && rand.nextBoolean()) || res_a.isEmpty()){ //
		 * send ping
		 * 
		 * val res = res_p(rand.nextInt(res_p.length)) val o_res = map_p(res)
		 * val rr = o_res.head m.step(('ping,List(ros(res),ros(rr)))) res_a +:=
		 * rr map_a += (rr -> (map_a.getOrElse(rr,Set[Int]())+res))
		 * 
		 * if(o_res.size==1){ map_p -= res res_p = res_p.filter{_!=res} } else
		 * map_p += ((res -> o_res.tail)) } else{ // send ack val res =
		 * res_a(rand.nextInt(res_a.length)) val o_res =map_a(res) val rr =
		 * o_res.head m.step(('ack,List(ros(res),ros(rr))))
		 * 
		 * if(o_res.size==1){ map_a -= res res_a = res_a.filter{_!=res} } else
		 * map_a += ((res -> o_res.tail)) } }
		 */
		// m.asInstanceOf[qea.MonitorConfig].getConfigs foreach println
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

			if ((sent < send_max) && (to_ack.isEmpty() || rand.nextBoolean())) {
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

	/*
	 * The methods that we override to call the monitor. Events of the
	 * properties
	 */

	public abstract void command(int x);

	public abstract void request(Object o);

	public abstract void grant(Object o);

	public abstract void deny(Object o);

	public abstract void rescind(Object o);

	public abstract void cancel(Object o);

	public abstract void succeed(Object o);

	public abstract void command(Object o);

	public abstract void set_ack_timeout(int x);

	public abstract void command(Object a, Object b, Object c, int d);

	public abstract void ack(Object o, int x);

	public abstract void grant(Object a, Object b);

	public abstract void cancel(Object a, Object b);

	public abstract void schedule(Object a, Object b);

	public abstract void finish(Object o);

	public abstract void conflict(Object a, Object b);

	public abstract void ping(Object a, Object b);

	public abstract void ack(Object a, Object b);

	public abstract void ack(Object a, Object b, int c);

	public abstract void send(Object a, Object b, int c);

}
