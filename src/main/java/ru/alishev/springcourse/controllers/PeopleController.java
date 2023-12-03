package ru.alishev.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.dao.PersonDAO;
import ru.alishev.springcourse.models.Person;

import java.sql.SQLException;

@Controller
@RequestMapping("/people")
public class PeopleController {
    //хотел передавать @Autowired непосредственно в атрибут, но СР порекомендовала так, @Autowired ниже можно даже удалить (хз почему так?)
//    @Autowired
    private final PersonDAO personDAO;

    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) throws SQLException {
//        //Получим всех людей из DAO и передадим на отображение в представление
        model.addAttribute("people", personDAO.index());
        return "people/index";
//        return null;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) throws SQLException {
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
    //Он так же кладет этот объект обратно в модель
    //@Valid проверяет атрибуты класса на указанную в них валидность, и если есть ошибки кладет их в объект BindingResult
    //Важно, чтобы этот объект шел в аргументах непосредственно за объектом с валидацией
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors())
            return "people/new";
        personDAO.save(person);
        //redirect: перенаправляет нас на страницу people (именно по get-запросу, видимо?)
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) throws SQLException {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
//    @RequestMapping(method = RequestMethod.PATCH, value="/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) throws SQLException {
        if (bindingResult.hasErrors())
            return "people/edit";
        personDAO.update(id, person);
//        return "redirect:/people";
        return "redirect:/people/" + id;  //new
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) throws SQLException {
        personDAO.delete(id);
        return "redirect:/people";
    }

}
