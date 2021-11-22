package com.models.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.models.entities.Contract;
import com.models.entities.Installment;

public class ContractService {
	
	private OnlinePaymentService onlinePaymentService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public ContractService(OnlinePaymentService onlinePaymentService) {
		super();
		this.onlinePaymentService = onlinePaymentService;
	}

	public void processContract(Contract contract, int months) throws ParseException {
		double installmentValueOutInterest = contract.getTotalValue() / months;
		List<Installment> installments = new ArrayList<>();
		Installment installment = null;
		
		// Preparando para trabalhar com datas
		Date contractDate = contract.getDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(contractDate);
		int month = calendar.get(Calendar.MONTH) + 2;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int year = calendar.get(Calendar.YEAR);
		
		double installmentValue = 0.0;
		for (int i = 1; i < months + 1; i++) {
			installmentValue = installmentValueOutInterest; // 200
			installment = new Installment();
			
			installmentValue += onlinePaymentService.interest(installmentValue, i); // 200 + 2 = 202
			installmentValue +=  onlinePaymentService.paymentFee(installmentValue); // 202 + 4.04 = 206.04
			
			installment.setAmount(installmentValue);
			installment.setDueDate(sdf.parse(day+"/"+month+"/"+year));
			
			installments.add(installment);
			month++;
		}	
		contract.setInstallments(installments);
	}
}
