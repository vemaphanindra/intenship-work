package com.collect.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.collect.data.entity.UserEntity;
import com.collect.data.entity.TaskEntity;
import com.collect.data.entity.ComplaintEntity;
import com.collect.data.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            UserEntity user = userService.findByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                return "redirect:/dashboard";
            } else {
                model.addAttribute("message", "Invalid username or password.");
                return "login";
            }
        } catch (Exception e) {
            logger.error("Error during login", e);
            model.addAttribute("message", "An error occurred. Please try again.");
            return "login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/forms")
    public String forms() {
        return "forms";
    }

    @GetMapping("/task")
    public String task() {
        return "Task";
    }

    @PostMapping("/saveTask")
    public String saveTask(@RequestParam String taskName, @RequestParam String taskDescription,
                           @RequestParam String assigneeName, @RequestParam String assigneeEmail,
                           @RequestParam("attachment") MultipartFile attachment, Model model) {
        try {
            TaskEntity task = new TaskEntity();
            task.setTaskName(taskName);
            task.setTaskDescription(taskDescription);
            task.setAssigneeName(assigneeName);
            task.setAssigneeEmail(assigneeEmail);
            task.setAttachment(attachment.getBytes());

            userService.saveTask(task);
            model.addAttribute("message", "Task saved successfully!");
            model.addAttribute("taskName", taskName);
        } catch (IOException e) {
            logger.error("Error saving task", e);
            model.addAttribute("message", "Error saving task. Please try again.");
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            model.addAttribute("message", "An unexpected error occurred. Please try again.");
        }
        return "Task";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @PostMapping("/getTask")
    public String getTask(@RequestParam String taskName, Model model) {
        TaskEntity task = userService.findTaskByName(taskName);
        if (task != null) {
            model.addAttribute("task", task);
        } else {
            model.addAttribute("message", "Task not found.");
        }
        return "admin";
    }

    @PostMapping("/getComplaint")
    public String getComplaint(@RequestParam String email, Model model) {
        ComplaintEntity complaint = userService.findComplaintByEmail(email);
        if (complaint != null) {
            model.addAttribute("complaint", complaint);
        } else {
            model.addAttribute("message", "Complaint not found.");
        }
        return "/admin";
    }

    @GetMapping("/complaint")
    public String complaint() {
        return "com";
    }

    @PostMapping("/submitComplaint")
    public String submitComplaint(@RequestParam String name, @RequestParam String phone,
                                  @RequestParam String email, @RequestParam String highlight,
                                  @RequestParam String complaint, @RequestParam String timeSlot,
                                  @RequestParam int rating, Model model) {
        try {
            ComplaintEntity complaintEntity = new ComplaintEntity();
            complaintEntity.setName(name);
            complaintEntity.setPhone(phone);
            complaintEntity.setEmail(email);
            complaintEntity.setHighlight(highlight);
            complaintEntity.setComplaint(complaint);
            complaintEntity.setTimeSlot(timeSlot);
            complaintEntity.setRating(rating);

            userService.saveComplaint(complaintEntity);
            model.addAttribute("message", "Complaint submitted successfully!");
        } catch (Exception e) {
            logger.error("Error submitting complaint", e);
            model.addAttribute("message", "An error occurred. Please try again.");
        }
        return "com";
    }
}
