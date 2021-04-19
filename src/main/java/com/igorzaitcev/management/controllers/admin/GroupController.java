package com.igorzaitcev.management.controllers.admin;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.service.GroupManagement;

@Controller
@RequestMapping("/admin/")
public class GroupController {
	GroupManagement management;
	private static final String URL = "admin/view/group-view";
	private static final String NEWURL = "admin/edit/group-edit";
	private static final String MESSAGE = "message";
	private static final String ATTRIBUTE = "group";
	private static final String PRINT = "groups";

	public GroupController(GroupManagement management) {
		this.management = management;
	}

	@GetMapping("view/group")
	public String getGroup(Model model) {
		return URL;
	}

	@GetMapping("view/findgroup")
	public String findGroup(@RequestParam("id") Optional<Long> id, Model model) {
		if (id.isEmpty()) {
			model.addAttribute(MESSAGE, "Id is not valid...");
		} else {
			management.findGroup(id.get()).ifPresentOrElse(group -> {
				model.addAttribute(PRINT, group);
			}, () -> {
				model.addAttribute(MESSAGE, "Group not found...");
			});
		}
		return URL;
	}

	@GetMapping(value = "edit/deletegroup/{id}")
	public String deleteGroup(@PathVariable Long id, Model model) {
		boolean deleted = false;
		try {
			deleted = management.deleteGroup(id);
		} catch (InvalidArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return URL;
		}
		if (deleted) {
			model.addAttribute(MESSAGE, "Group deleted...");
		} else {
			model.addAttribute(MESSAGE, "Group not found...");
		}
		return URL;
	}

	@GetMapping("view/showgroup")
	public String showGroup(@RequestParam("offset") Optional<Integer> offset,
			@RequestParam("limit") Optional<Integer> limit,
			Model model) {
		if (offset.isEmpty() || limit.isEmpty()) {
			model.addAttribute(PRINT, management.getGroupsByLimit(1, 50));
		} else {
			model.addAttribute(PRINT, management.getGroupsByLimit(offset.get(), limit.get()));
		}
		return URL;
	}

	@GetMapping("view/addgroup")
	public String newGroupForm(Model model) {
		model.addAttribute(ATTRIBUTE, new Group());
		return NEWURL;
	}

	@GetMapping(value = "view/editgroup/{id}")
	public String editGroup(@PathVariable Long id, Model model) {
		Optional<Group> group = management.findGroup(id);
		model.addAttribute(ATTRIBUTE, group.get());
		return NEWURL;
	}

	@PostMapping("edit/addgroup")
	public String addGroup(@Valid @ModelAttribute(ATTRIBUTE) Group group, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return NEWURL;
		}
		try {
			Group addedGroup = management.addGroup(group);
			model.addAttribute(PRINT, addedGroup);
		} catch (MissingArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return NEWURL;
		}
		return getGroup(model);
	}
}
