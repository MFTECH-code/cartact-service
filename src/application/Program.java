package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.models.entities.Contract;
import com.models.entities.Installment;
import com.models.services.ContractService;
import com.models.services.PaypalService;

public class Program {

	public static void main(String[] args) throws ParseException {
		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Contract contract = null;
		ContractService contractService = null;
		
		System.out.println("Enter contract data");
		System.out.print("Number: ");
		int number = sc.nextInt();
		sc.nextLine();
		System.out.print("Date (dd/MM/yyyy): ");
		Date date = sdf.parse(sc.nextLine());
		System.out.print("Contract value: ");
		double contractValue = sc.nextDouble();
		List<Installment> installments = null;
		contract = new Contract(number, date, contractValue, installments);
		
		System.out.print("Enter the number of installments: ");
		int numberOfInstallments = sc.nextInt();
		contractService = new ContractService(new PaypalService());
		contractService.processContract(contract, numberOfInstallments);
		
		System.out.println("Installments:");
		for (Installment installment : contract.getInstallments()) {
			System.out.println(sdf.format(installment.getDueDate()) + " - " + String.format("%.2f", installment.getAmount()));
		}
		
		sc.close();
	}

}
