import java.io.IOException;
import java.util.Scanner;

public class Main
{
	
	public static void entrerCommande(Scanner s) throws IOException {
		System.out.println("*********************************************************************");
		System.out.println("																	*");
		System.out.println("				MINI_SGBD											 ");
		System.out.println("																	*");
		System.out.println("*********************************************************************");
		System.out.println("Entrez votre commande");
		String commande = s.nextLine();
		while(commande.equals("EXIT") == false)
		{
			DBManager.getInstance().ProcessCommand(commande);
			System.out.println("Entrez votre commande");
			commande = s.nextLine();
		}
	}
	public static void main (String[] args) throws IOException {
		DBParams.DBPath = args[0];
		DBParams.pageSize = 4096;
		DBParams.frameCount = 2;
		DBManager.getInstance().Init();
		Scanner s = new Scanner(System.in);
		entrerCommande(s);
		s.close();
		DBManager.getInstance().Finish();
	}
}
	