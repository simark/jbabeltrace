import org.eclipse.linuxtools.ctf.core.event.EventDefinition;
import org.eclipse.linuxtools.ctf.core.trace.CTFTrace;
import org.eclipse.linuxtools.ctf.core.trace.CTFTraceReader;
import org.eclipse.linuxtools.ctf.core.event.types.IntegerDefinition;
import org.eclipse.linuxtools.ctf.core.event.types.StructDefinition;

public class jbabeltrace {
	public static void main(String args[]) {
		if (args.length != 1) {
			System.err.println("Usage: jbabeltrace [trace folder]");
			System.exit(1);
		}

		String path = args[0];

		try {
			CTFTrace trace = new CTFTrace(path);
			CTFTraceReader reader = new CTFTraceReader(trace);

			while (reader.hasMoreEvents()) {
				EventDefinition currentEvent = reader.getCurrentEventDef();
				StructDefinition currentPacketContext = currentEvent.getPacketContext();

				System.out.print("[" + currentEvent.getTimestamp() + "] ");
				System.out.print(currentEvent.getDeclaration().getName() + ": ");

				IntegerDefinition cpu_id = (IntegerDefinition) currentPacketContext.lookupDefinition("cpu_id");
				System.out.print("{ cpu_id = " + cpu_id.getValue() + " }, ");

				StructDefinition fields = currentEvent.getFields();
				System.out.print(fields.toString());

				System.out.println();

				reader.advance();
			}

			System.out.println("Done");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
