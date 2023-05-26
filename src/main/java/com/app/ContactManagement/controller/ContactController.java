package com.app.ContactManagement.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.ContactManagement.model.Contact;
import com.app.ContactManagement.repository.ContactRepository;
import com.app.ContactManagement.service.ContactServiceImpl;
import com.app.ContactManagement.service.MyUserDetails;

@Controller
public class ContactController {

	@Autowired
	private ContactServiceImpl contactServiceImpl;

	@Autowired
	private ContactRepository contactRepository;
	
	@PostMapping("/addContact")
	public String showContactList(@RequestParam(name = "first_name") String fname,
			@RequestParam(name = "last_name") String lname, @RequestParam(name = "phone1") String phone1,
			@RequestParam(name = "phone2") String phone2, @RequestParam(name = "address") String address,
			@RequestParam(name = "email_professional") String email_professional,
			@RequestParam(name = "email_personal") String email_personal, @RequestParam(name = "gender") String gender,
			Model model, Principal principal) {

		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		Contact c = new Contact(fname, lname, phone1, phone2, address, email_professional, email_personal, gender,
				userDetails.getUser());

		contactServiceImpl.addContact(c);
		
		List<Contact> contacts = contactServiceImpl.findByUserId(userDetails.getUser());

		model.addAttribute("contacts", contacts);

		return "redirect:/add";
	}
	
	@PostMapping("/updateContact")
	public String updateContact(@RequestParam(name="contactidtoupdate") String contactId, @ModelAttribute("contactToUpdate") Contact contact,
			Model model) {
		System.out.println("Contacteded: "+contactId);
		Contact c = contactRepository.findByid(Long.parseLong(contactId));
		c.setAddress(contact.getAddress());
		c.setEmail_personal(contact.getEmail_personal());
		c.setEmail_professional(contact.getEmail_professional());
		c.setFirst_name(contact.getFirst_name());
		c.setGender(contact.getGender());
		c.setLast_name(contact.getLast_name());
		c.setPhone1(contact.getPhone1());
		c.setPhone2(contact.getPhone2());
		
		contactRepository.flush();

		return "redirect:/update";
	}
	
}
