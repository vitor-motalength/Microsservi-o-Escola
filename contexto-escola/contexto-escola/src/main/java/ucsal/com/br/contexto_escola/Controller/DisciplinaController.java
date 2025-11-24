package ucsal.com.br.contexto_escola.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ucsal.com.br.contexto_escola.DTO.DisciplinaDTO;
import ucsal.com.br.contexto_escola.Service.DisciplinaService;

import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {
    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @PostMapping
    public ResponseEntity<DisciplinaDTO> create(@Validated @RequestBody DisciplinaDTO dto) {
        DisciplinaDTO created = disciplinaService.createDisciplina(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    //  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
    @GetMapping
    public List<DisciplinaDTO> list() {
        return disciplinaService.listDisciplinas();
    }

    //   @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
    @GetMapping("/{id}")
    public DisciplinaDTO get(@PathVariable Long id) {
        return disciplinaService.getDisciplina(id);
    }

    //   @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public DisciplinaDTO update(@PathVariable Long id, @Validated @RequestBody DisciplinaDTO dto) {
        return disciplinaService.updateDisciplina(id, dto);
    }

    //  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/change-status")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        DisciplinaDTO disc = disciplinaService.getDisciplina(id);
        DisciplinaDTO updatedDisc = new DisciplinaDTO(
                disc.id(),
                disc.sigla(),
                disc.descricao(),
                disc.cargaHoraria(),
                disc.cursoId(),
                disc.matriz(),
                !disc.ativo()
        );
        disciplinaService.updateDisciplina(id, updatedDisc);
        return ResponseEntity.noContent().build();
    }
}
