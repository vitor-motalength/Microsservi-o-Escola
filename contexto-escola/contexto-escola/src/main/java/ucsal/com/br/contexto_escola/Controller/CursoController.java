package ucsal.com.br.contexto_escola.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ucsal.com.br.contexto_escola.DTO.CursoDTO;
import ucsal.com.br.contexto_escola.Service.CursoService;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }


    @PostMapping
    public ResponseEntity<CursoDTO> create(@Validated @RequestBody CursoDTO dto) {
        CursoDTO created = cursoService.createCurso(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @GetMapping
    public List<CursoDTO> list() {
        return cursoService.listCursos();
    }


    @GetMapping("/{id}")
    public CursoDTO get(@PathVariable Long id) {
        return cursoService.getCurso(id);
    }


    @PutMapping("/{id}")
    public CursoDTO update(@PathVariable Long id, @Validated @RequestBody CursoDTO dto) {
        return cursoService.updateCurso(id, dto);
    }


    @PatchMapping("/{id}/change-status")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        cursoService.changeStatusCurso(id);
        return ResponseEntity.noContent().build();
    }
}
