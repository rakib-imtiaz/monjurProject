package edu.diu.aoop8;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.diu.aoop8.model.Account;

@Controller
public class MyController {
	@Autowired
	AccountService service;

	@RequestMapping("/")
	public String showRoot() {
		return "index";
	}

	@RequestMapping("/home")
	public String showMyHome(Model model) {
		return "home";
	}

	@GetMapping("/registrationPage")
	public String signUpPage() {
		return "signup.html";

	}

	@GetMapping("/login")
	public String loginPage() {
		return "login.html";

	}

	@PostMapping("/addNewAccount")
	public String saveData(@ModelAttribute Account account) {
		System.out.println(account);

		service.save(account);
		// System.out.println(account);
		System.out.println(account.getAccountHolderName());
		return "redirect:/";
	}

	@RequestMapping("/viewAllCustomer")

	public String viewAllCustomers(Model model) {
		List<Account> listAccounts = service.listAll();

		model.addAttribute("listAccounts", listAccounts);
		return "allCustomers.html";
	}

	@PostMapping("/userAuthentication")
	public ModelAndView loginCredentials(@RequestParam String email, @RequestParam String password) {

		// System.out.println("user" + email);
		// System.out.println("password" + password);

		List<Account> accountList = service.listAll();

		String tempmail, pass;
		boolean flag = false;
		int index = -1;

		/*
		 * for(int i=0;i<accountList.size();i++) {
		 * if(accountList.get(i).getEmail()==null) {
		 * service.delete(accountList.get(i).getAccountID()); } }
		 */
		// fetchin user credentials... and matching with the crossponding password

		for (int i = 0; i < accountList.size(); i++) {
			if (accountList.get(i).getEmail().contentEquals(email)) {
				if (accountList.get(i).getPassword().contentEquals(password)) {
					flag = true;
					index = i;
					break;
				}
			}

		}

		// System.out.println(accountList.get(index));

		Account obj = null;

		ModelAndView mv = new ModelAndView();

		if (flag == true) {
			obj = accountList.get(index);
			mv.setViewName("home.html");
			mv.addObject("data", obj); // adding object name as data

		} else {
			mv.setViewName("errorpage.html");
			mv.addObject("data", obj); // adding object name as data

		}

		return mv;

	}

	@RequestMapping("/transferMoney")
	String viewTransferMoneyPage() {

		// System.out.println("in the transfer...");

		return "transferMoney.html";
	}

	boolean isAccoutAvailable(List<Account> list, String accountNumber) {
		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getAccountNumber().contentEquals(accountNumber)) {
				return true;
			}

		}
		return flag;
	}

	boolean isAmountTransactionoable(List<Account> list, String accountNumber, double amountToSend) {
		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getAccountNumber().contentEquals(accountNumber)) {

				if (list.get(i).getBalance() >= amountToSend) {
					// checking whether the current balance is greater or equal to the amount to be
					// send to another user.
					return true;

				}
			}

		}
		return flag;
	}

	@PostMapping("/performTransaction")
	public String processTranscation(@RequestParam String senderAccountNumber,
			@RequestParam String recieverAccountNumber, @RequestParam String password,
			@RequestParam double amountToSend, Model model) {
		System.out.println(senderAccountNumber);
		System.out.println(recieverAccountNumber);
		System.out.println(amountToSend);
		System.out.println(password);

		List<Account> currentData = service.listAll();
		boolean isSenderAvailable = isAccoutAvailable(currentData, senderAccountNumber);

		boolean isReciverAvailable = isAccoutAvailable(currentData, recieverAccountNumber);

		boolean isAmountSendable = isAmountTransactionoable(currentData, senderAccountNumber, amountToSend);

		int senderIndex = -1;
		int recieverIndex = -1;
		ModelAndView mv = new ModelAndView();
		
		System.out.println("amount: "+isAmountSendable);
		System.out.println("Reciever: "+isReciverAvailable);
		System.out.println("sender: "+isSenderAvailable);



		if (isSenderAvailable == true && isReciverAvailable == true && isAmountSendable == true) {
			// adding balance to recievers aaccount

			for (int i = 0; i < currentData.size(); i++) {
				if (currentData.get(i).getAccountNumber().contentEquals(recieverAccountNumber)) {
					double currentBalance = currentData.get(i).getBalance();
					double newBalance = currentBalance + amountToSend;

					currentData.get(i).setBalance(newBalance);
					recieverIndex = i;
				}
			}

			// removing balance from sender

			for (int i = 0; i < currentData.size(); i++) {
				if (currentData.get(i).getAccountNumber().contentEquals(senderAccountNumber)) {
					double currentBalance = currentData.get(i).getBalance();
					double newBalance = currentBalance - amountToSend;

					currentData.get(i).setBalance(newBalance);
					senderIndex = i;
				}
			}


			// deleting previous datas from 
			service.repo.deleteAll();

//			for (int i = 0; i < currentData.size(); i++) {
//				service.delete(currentData.get(i).getAccountID());
//
//			}
			// adding datas to database
			for (int i = 0; i < currentData.size(); i++) {
				service.save(currentData.get(i));

			}

//			service.delete(currentData.get(senderIndex).getAccountID());
//			service.delete(currentData.get(recieverIndex).getAccountID());
//
//			System.out.println("data deleted...");
//
//			service.save(currentData.get(senderIndex));
//			service.save(currentData.get(recieverIndex));
//			System.out.println("data added newly...");

			// adding data to the model
			model.addAttribute("listAccounts", currentData);

			for (int i = 0; i < currentData.size(); i++) {
				mv.addObject(currentData.get(i));
				System.out.println("Data : " + currentData.get(i));
			}
			return "allCustomers.html";

		}

		else {

			return "errorpage.html";
		}

	}

	@RequestMapping(value = "/updateAccount", method = RequestMethod.POST)
	public String updateProduct(@ModelAttribute("Account") Account Account) {

		Account p = service.get(Account.getAccountID());
		p.setAccountHolderName(Account.getAccountHolderName());
		p.setAccountNumber(Account.getAccountNumber());
		p.setEmail(Account.getEmail());
		p.setBalance(Account.getBalance());
		service.save(p);

		return "redirect:/";
	}

	@RequestMapping("/editAccount/{id}")
	public ModelAndView showEditProductPage(@PathVariable(name = "id") int id) {
		ModelAndView mav = new ModelAndView("edit_Account");
		Account Account = service.get(id);
		mav.addObject("Account", Account);

		return mav;
	}

	@RequestMapping("/deleteAccount/{id}")
	public String deleteAccount(@PathVariable(name = "id") int id) {
		service.delete(id);
		return "redirect:/";
	}

}
