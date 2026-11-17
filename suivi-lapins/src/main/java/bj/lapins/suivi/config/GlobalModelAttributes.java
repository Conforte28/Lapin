package bj.lapins.suivi.config;

import bj.lapins.suivi.entity.ParametreExploitation;
import bj.lapins.suivi.service.ParametreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    private final ParametreService parametreService;

    public GlobalModelAttributes(ParametreService parametreService) {
        this.parametreService = parametreService;
    }

    @ModelAttribute("exploitation")
    public ParametreExploitation ajouterExploitation() {
        return parametreService.obtenirParametres();
    }

    @ModelAttribute("uriCourante")
    public String ajouterUriCourante(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
