package com.models.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
		Calendar contractDate = contract.getDate();
		int month = Calendar.MONTH + 2;
		List<Installment> installments = new ArrayList<>();
		Installment installment = null;
		double installmentValue = 0.0;
		for (int i = 1; i < months; i++) {
			installment = new Installment();
			installmentValue = installmentValueOutInterest + onlinePaymentService.interest(installmentValue, i);
			installmentValue += onlinePaymentService.paymentFee(installmentValue);
			installment.setAmount(installmentValue);
			installment.setDueDate(sdf.parse(contractDate.DAY_OF_MONTH+"/"+month+"/"+contractDate.YEAR));
			installments.add(installment);
			month++;
		}	
		contract.setInstallments(installments);
	}
}
