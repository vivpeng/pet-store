package com.example.controller;
/**
 * Created by pengg on 5/31/2016.
 */

import com.example.domain.*;
import com.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/")
public class PetStoreController {

  @Autowired
  private PetRepository petRepository;
  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private TagRepository tagRepository;
  @Autowired
  private PhotoRepository photoRepository;

  private final AtomicLong counterPet = new AtomicLong();
  private final AtomicLong counterCategory = new AtomicLong();
  private final AtomicLong counterTag = new AtomicLong();

  @RequestMapping("/")
  public String test1(Model model) {
    return "redirect:/home";
  }

  @RequestMapping("/welcom")
  public String welcome(Map<String, Object> model) {
    model.put("time", new Date());
    model.put("message", "save");
    return "test";
  }

  @RequestMapping("/home")
  public ModelAndView test1() {
//    model.addAttribute("message", "save2");
//    return new ModelAndView("list", "message", "save");
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    ModelAndView mv = new ModelAndView("list");
    mv.addObject("user", auth.getName());
    return mv;
//    return "list";
  }

  @RequestMapping("/exception")
  public ModelAndView test2() {
    ModelAndView mv = new ModelAndView("exception");
    mv.addObject("message", "test error");
    return mv;
  }

  @RequestMapping("/allPets")
  @ResponseBody
  public ArrayList<Pet> pets() {
    ArrayList<Pet> pets = new ArrayList();
    petRepository.findAllByOrderByIdDesc().forEach(pet -> pets.add(pet));
    return pets;
  }

  @RequestMapping("/pet/findByTags")
  @ResponseBody
  public HashSet<Pet> findByTags(@RequestParam(value="tags", defaultValue="") String tagsStr) {
    HashSet<Pet> pets = new HashSet();
    if (tagsStr != null && tagsStr.length() > 0) {
      for(String tagName : tagsStr.split(",")) {
          pets.addAll(petRepository.searchByTagName(tagName));
      }
    }
    return pets;
  }

  @RequestMapping("/allTags")
  @ResponseBody
  public ArrayList<Tag> tags() {
    ArrayList<Tag> tags = new ArrayList();
    tagRepository.findAll().forEach(tag -> tags.add(tag));
    return tags;
  }

  @RequestMapping("/tags/{searchTerm}")
  @ResponseBody
  public ArrayList<Tag> searchTags(@PathVariable("searchTerm") String searchTerm) {
    ArrayList<Tag> tags = new ArrayList();
    if (searchTerm != "" && searchTerm != null) {
      if (searchTerm.contains(",")) {
        if (searchTerm.lastIndexOf(",") == searchTerm.length() - 1) return tags;
        searchTerm = searchTerm.split(",")[searchTerm.split(",").length - 1];
      }
      tagRepository.searchByTerm("%" + searchTerm + "%").forEach(tag -> tags.add(tag));
    }
    return tags;
  }


  @RequestMapping("/allCategories")
  @ResponseBody
  public ArrayList<Category> categories() {
    ArrayList<Category> categories = new ArrayList();
    categoryRepository.findAll().forEach(category -> categories.add(category));
    return categories;
  }

  @RequestMapping(value = "/pet/{id}", method = RequestMethod.DELETE)
  @ResponseBody
  @Secured("ROLE_ADMIN")
  public Map delPet(@PathVariable("id") Long id) {
    HashMap map = new HashMap();
    try {
      Pet pet = petRepository.findById(id);
      petRepository.delete(pet);
      map.put("msg", "ok");
      return map;
    }
    catch (Exception e) {
      map.put("msg", e.toString());
      return map;
    }
  }

  @RequestMapping(value = "/pet/{id}", method = RequestMethod.GET)
  @ResponseBody
  @Secured("ROLE_ADMIN")
  public Object getPet(@PathVariable("id") Long id) {
    try {
      Pet pet = petRepository.findById(id);
      return pet;
    }
    catch (Exception e) {
      HashMap map = new HashMap();
      map.put("message", e.toString());
      return map;
    }
  }

  @RequestMapping(value="/pet", method = RequestMethod.POST)
  @Secured("ROLE_USER")
  public ModelAndView pet(@RequestParam(value="name", defaultValue="Kitty") String name,
                          @RequestParam(value="status", defaultValue="true") String status,
                          @RequestParam(value="category", defaultValue="1") Long categoryId,
                          @RequestParam(value="newCategoryName", defaultValue="categoryName") String newCategoryName,
                          @RequestParam(value="tags", defaultValue="") String tagsStr,
                          @RequestParam("photos") MultipartFile[] photos, RedirectAttributes redirectAttributes) {

    ModelAndView mv = new ModelAndView("redirect:/home");
    try {
      Category category;
      if (categoryId == 0) {
        category = (new Category(newCategoryName));
      }
      else {
        category = categoryRepository.findById(categoryId);
      }
      //categoryRepository.save(category);//categoryRepository.findByName("cat1").get(0)
      Pet pet = new Pet(name, true, category);
      HashSet<Tag> tags = new HashSet<Tag>();
      if (tagsStr != null && tagsStr.length() > 0) {
        for(String tagName : tagsStr.split(",")) {
          Tag t = tagRepository.findByName(tagName);
          if (t != null) {
            tags.add(t);
          } else {
            tags.add(new Tag(tagName));
          }
        }
      }
	    pet.setTags(tags);

      if (photos.length > 0) {
        String urlStr = new String(), phsicalUrl = new String(), placedUrl = new String();
        UUID randomUUID;
        HashSet<PhotoUrl> photoUrls = new HashSet<PhotoUrl>();
        for(MultipartFile photo : photos) {
          byte[] bytes = photo.getBytes();
          if (bytes.length > 0)
          {
            randomUUID = UUID.randomUUID();
            urlStr = "images/" + randomUUID + photo.getOriginalFilename();
            phsicalUrl = "src/main/resources/public/images/" + randomUUID + photo.getOriginalFilename();
            placedUrl = "target/classes/public/images/" + randomUUID + photo.getOriginalFilename();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(phsicalUrl)));
            stream.write(bytes);
            stream = new BufferedOutputStream(new FileOutputStream(new File(placedUrl)));
            stream.write(bytes);
            stream.close();
            //photoRepository.save(new PhotoUrl(url, pet));
            photoUrls.add(new PhotoUrl(urlStr, pet));
          }
        }
        pet.setPhotoUrls(photoUrls);
      }
      petRepository.save(pet);
      redirectAttributes.addFlashAttribute("message", "A new pet is added successfully!!");
//      mv.addObject("message", "A new pet is added successfully!!");
    }
    catch (Exception ex) {
      redirectAttributes.addFlashAttribute("error", ex.toString());
//      mv.addObject("error", ex.toString());
    }
    finally {
      return mv;
    }
  }

}