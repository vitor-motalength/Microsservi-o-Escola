package ucsal.com.br.contexto_escola.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucsal.com.br.contexto_escola.DTO.CursoDTO;
import ucsal.com.br.contexto_escola.Entity.Curso;
import ucsal.com.br.contexto_escola.Entity.Escola;
import ucsal.com.br.contexto_escola.Exception.BadRequestException;
import ucsal.com.br.contexto_escola.Exception.ResourceNotFoundException;
import ucsal.com.br.contexto_escola.Repository.CursoRepository;
import ucsal.com.br.contexto_escola.Repository.EscolaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CursoService {
    private final CursoRepository cursoRepository;
    private final EscolaRepository escolaRepository;

    public CursoService(CursoRepository cursoRepository, EscolaRepository escolaRepository) {
        this.cursoRepository = cursoRepository;
        this.escolaRepository = escolaRepository;
    }

    public CursoDTO createCurso(CursoDTO dto) {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new BadRequestException("Nome do curso é obrigatório");
        }
        if (cursoRepository.existsByNome(dto.nome())) {
            throw new BadRequestException("Já existe um curso com o nome " + dto.nome());
        }
        Escola escola = escolaRepository.findById(dto.escolaId())
                .orElseThrow(() -> new ResourceNotFoundException("Escola não encontrada com id " + dto.escolaId()));
        Curso curso = new Curso(dto.nome(), escola, dto.ativo());
        cursoRepository.save(curso);
        return toDto(curso);
    }

    public CursoDTO getCurso(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com id " + id));
        return toDto(curso);
    }

    public List<CursoDTO> listCursos() {
        return cursoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CursoDTO updateCurso(Long id, CursoDTO dto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com id " + id));
        if (dto.nome() != null && !dto.nome().trim().isEmpty()) {
            if (!curso.getNome().equals(dto.nome()) && cursoRepository.existsByNome(dto.nome())) {
                throw new BadRequestException("Já existe um curso com o nome " + dto.nome());
            }
            curso.setNome(dto.nome());
        }
        if (dto.escolaId() != null) {
            Escola escola = escolaRepository.findById(dto.escolaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Escola não encontrada com id " + dto.escolaId()));
            curso.setEscola(escola);
        }
        curso.setAtivo(dto.ativo());
        return toDto(cursoRepository.save(curso));
    }

    public void changeStatusCurso(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com id " + id));
        curso.setAtivo(!curso.getAtivo());
        cursoRepository.save(curso);
    }

    private CursoDTO toDto(Curso curso) {
        return new CursoDTO(curso.getId(), curso.getNome(), curso.getEscola().getId(), curso.isAtivo());
    }
}