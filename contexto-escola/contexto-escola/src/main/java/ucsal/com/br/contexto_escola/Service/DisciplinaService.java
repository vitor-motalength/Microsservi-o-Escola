package ucsal.com.br.contexto_escola.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucsal.com.br.contexto_escola.DTO.DisciplinaDTO;
import ucsal.com.br.contexto_escola.Entity.Curso;
import ucsal.com.br.contexto_escola.Entity.Disciplina;
import ucsal.com.br.contexto_escola.Exception.BadRequestException;
import ucsal.com.br.contexto_escola.Exception.ResourceNotFoundException;
import ucsal.com.br.contexto_escola.Repository.CursoRepository;
import ucsal.com.br.contexto_escola.Repository.DisciplinaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DisciplinaService {
    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository, CursoRepository cursoRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.cursoRepository = cursoRepository;
    }

    public DisciplinaDTO createDisciplina(DisciplinaDTO dto) {
        if (dto.sigla() == null || dto.sigla().trim().isEmpty()) {
            throw new BadRequestException("Sigla da disciplina é obrigatória");
        }
        if (disciplinaRepository.existsBySigla(dto.sigla())) {
            throw new BadRequestException("Já existe uma disciplina com a sigla " + dto.sigla());
        }
        Curso curso = cursoRepository.findById(dto.cursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com id " + dto.cursoId()));
        Disciplina disciplina = new Disciplina(dto.sigla(), dto.descricao(), dto.cargaHoraria(), curso, dto.matriz());
        disciplinaRepository.save(disciplina);
        return toDto(disciplina);
    }

    public DisciplinaDTO getDisciplina(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com id " + id));
        return toDto(disciplina);
    }

    public List<DisciplinaDTO> listDisciplinas() {
        return disciplinaRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public DisciplinaDTO updateDisciplina(Long id, DisciplinaDTO dto) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com id " + id));
        if (dto.sigla() != null && !dto.sigla().trim().isEmpty()) {
            if (!disciplina.getSigla().equals(dto.sigla()) && disciplinaRepository.existsBySigla(dto.sigla())) {
                throw new BadRequestException("Já existe uma disciplina com a sigla " + dto.sigla());
            }
            disciplina.setSigla(dto.sigla());
        }
        if (dto.descricao() != null) {
            disciplina.setDescricao(dto.descricao());
        }
        if (dto.cargaHoraria() != null) {
            disciplina.setCargaHoraria(dto.cargaHoraria());
        }
        if (dto.cursoId() != null) {
            Curso curso = cursoRepository.findById(dto.cursoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com id " + dto.cursoId()));
            disciplina.setCurso(curso);
        }
        if (dto.matriz() != null) {
            disciplina.setMatriz(dto.matriz());
        }
        disciplina.setAtivo(dto.ativo());
        disciplinaRepository.save(disciplina);
        return toDto(disciplina);
    }

    private DisciplinaDTO toDto(Disciplina disciplina) {
        return new DisciplinaDTO(disciplina.getId(), disciplina.getSigla(), disciplina.getDescricao(), disciplina.getCargaHoraria(), disciplina.getCurso().getId(), disciplina.getMatriz(), disciplina.isAtivo());
    }
}
