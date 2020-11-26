package calculateur.test;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

public class TestMXParser {

	public static void main(String[] args) {

		// info -> http://mathparser.org/mxparser-tutorial/user-defined-functions/
		Function fx = new Function("f(x)=x+x+2");
		System.out.println("x+x+2 = " + fx.calculate(3));

		// Pour connaitre toutes les fonctions internes disponibles ->
		// http://mathparser.org/api/org/mariuszgromada/math/mxparser/mathcollection/MathFunctions.html
		// Pour connaitre toutes les constantes internes disponibles ->
		// http://mathparser.org/mxparser-tutorial/built-in-constants/
		Function fx2 = new Function("f(x)=sin(x)");
		System.out.println("sin(pi/2)  = " + fx2.calculate(new Expression("pi/2").calculate()));
		
		// inf0 ->
		Function fxa = new Function("f(x,a)=x+a+2");
		System.out.println("x+a+2 = " + fxa.calculate(3, 2));

		// inf0-> http://mathparser.org/mxparser-tutorial/simple-expressions/
		Expression ex1 = new Expression("2+2*5");

		System.out.println("2+2*5 = " + ex1.calculate());
		
		// fonctin mal saisie
		Function fxMal = new Function("f(x)=x..+3");
		System.out.println("une fonction mal saisie donne " + fxMal.calculate(3));
		
		
		
	}
}
