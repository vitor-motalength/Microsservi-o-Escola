package ucsal.com.br.contexto_escola.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ucsal.com.br.contexto_escola.DTO.EscolaDTO;
import ucsal.com.br.contexto_escola.Service.EscolaService;


import java.util.List;

@RestController
@RequestMapping("/api/escolas")
public class EscolaController {
    private final EscolaService escolaService;

    public EscolaController(EscolaService escolaService) {
        this.escolaService = escolaService;
    }

    //  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<EscolaDTO> create(@Validated @RequestBody EscolaDTO dto) {
        EscolaDTO created = escolaService.createEscola(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
    @GetMapping
    public List<EscolaDTO> list() {
        return escolaService.listEscolas();
    }

    //  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
    @GetMapping("/{id}")
    public EscolaDTO get(@PathVariable Long id) {
        return escolaService.getEscola(id);
    }

    //  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public EscolaDTO update(@PathVariable Long id, @Validated @RequestBody EscolaDTO dto) {
        return escolaService.updateEscola(id, dto);
    }

    //   @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        escolaService.deactivateEscola(id);
        return ResponseEntity.noContent().build();
    }
}
