package ru.alishev.springcourse.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.dao.PersonDAO;
import ru.alishev.springcourse.models.Person;

@Controller
@RequestMapping("/people")
public class PeopleController {
    //хотел передавать @Autowired непосредственно в атрибут, но СР порекомендовала так, @Autowired ниже можно даже удалить (хз почему так?)
    private final PersonDAO personDAO;

//    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
//        //Получим всех людей из DAO и передадим на отображение в представление
        model.addAttribute("people", personDAO.index());
        return "people/index";
//        return null;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        System.out.println(id);

        //Получим одного человека по id из DAO и передадим на отображение в представление
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    @GetMapping("/new")
    //т.к. вызываем форму таймлифа, то ему нужно передать объект тот объект для которого эта форма нужна
    //здесь тоже можено использовать @ModelAttribute, т.к. заполняем поля объекта person. Алишев говорит что @ModelAttribute сама заполняет объект, получается все таки не с формы мы получаем заполненный объект... Или как блять.
//    public String newPerson(Model model) {
//        model.addAttribute("person", new Person());
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    //@ModelAttribute("person") автоматически получает из html-формы таймлифа объект person со всеми его заполненными атрибутами и кладет их наш объект Person person
    public String create(@ModelAttribute("person") Person person){
        personDAO.save(person);
        //redirect: перенаправляет нас на страницу people (именно по get-запросу, видимо?)
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
//    @RequestMapping(method = RequestMethod.PATCH, value="/{id}")
    public String update(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
        personDAO.update(id, person);
//        return "redirect:/people";
        return "redirect:/people/" + id;  //new
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }

}
