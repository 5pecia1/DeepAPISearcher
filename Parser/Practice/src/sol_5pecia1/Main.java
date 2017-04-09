package sol_5pecia1;

import java.util.Arrays;
import java.util.List;

public class Main {
	private static List<StartFunction> startFunctinos = Arrays.asList(
//			new GetSequence(),
			new GetThesisSequence()
			);
	
	public static void main(String[] args) {
		startFunctinos.forEach(function -> {
			function.start();
			System.out.println("===== next function ======");
		});
	}
}
