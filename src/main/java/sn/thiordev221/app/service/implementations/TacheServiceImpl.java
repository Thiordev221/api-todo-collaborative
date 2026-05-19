package sn.thiordev221.app.service.implementations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.thiordev221.app.dto.requests.TacheCreateRequest;
import sn.thiordev221.app.dto.requests.TacheUpdateRequest;
import sn.thiordev221.app.dto.responses.TacheResponse;
import sn.thiordev221.app.mapper.TacheMapper;
import sn.thiordev221.app.repository.TacheRepository;
import sn.thiordev221.app.service.contrats.TacheService;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TacheServiceImpl implements TacheService{

    private final TacheRepository tacheRepository;
    private final TacheMapper tacheMapper;
    
    @Override
    public TacheResponse ajouterTache(Long listId, TacheCreateRequest request, Long currentUserId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TacheResponse modifierTache(Long tacheId, TacheUpdateRequest request, Long currentUserId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void supprimerTache(Long tacheId, Long currentUserId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Page<TacheResponse> getTachesDeLaListe(Long listId, Long currentUserId, Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TacheResponse toggleStatus(Long tacheId, Long currentUserId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
