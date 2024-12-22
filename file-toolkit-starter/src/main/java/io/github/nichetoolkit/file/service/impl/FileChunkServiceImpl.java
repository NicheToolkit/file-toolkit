package io.github.nichetoolkit.file.service;

import io.github.nichetoolkit.file.PartEntity;
import io.github.nichetoolkit.file.FileFilter;
import io.github.nichetoolkit.file.mapper.FileChunkMapper;
import io.github.nichetoolkit.file.PartModel;
import io.github.nichetoolkit.rest.RestException;
import io.github.nichetoolkit.rest.util.GeneralUtils;
import io.github.nichetoolkit.rice.RiceIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>FileChunkServiceImpl</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
@Service
public class FileChunkServiceImpl extends RiceIdService<PartModel, PartEntity, FileFilter> implements FileChunkService {

    @Override
    public PartModel queryByFileIdAndChunkIndex(String fileId, Integer chunkIndex) throws RestException {
        if (GeneralUtils.isEmpty(fileId) || GeneralUtils.isEmpty(chunkIndex)) {
            return null;
        }
        PartEntity entity = ((FileChunkMapper) superMapper).findByFileIdAndChunkIndex(fileId, chunkIndex);
        if (GeneralUtils.isNotEmpty(entity)) {
            return modelActuator(entity);
        }
        return null;
    }

    @Override
    public PartModel queryByFileIdFirstChunk(String fileId) throws RestException {
        if (GeneralUtils.isEmpty(fileId)) {
            return null;
        }
        PartEntity entity = ((FileChunkMapper) superMapper).findByFileIdFirstChunk(fileId);
        if (GeneralUtils.isNotEmpty(entity)) {
            return modelActuator(entity);
        }
        return null;
    }

    @Override
    public PartModel queryByFileIdLastChunk(String fileId) throws RestException {
        if (GeneralUtils.isEmpty(fileId)) {
            return null;
        }
        PartEntity entity = ((FileChunkMapper) superMapper).findByFileIdLastChunk(fileId);
        if (GeneralUtils.isNotEmpty(entity)) {
            return modelActuator(entity);
        }
        return null;
    }

    @Override
    public List<PartModel> queryAllByFileId(String fileId) throws RestException {
        if (GeneralUtils.isEmpty(fileId)) {
            return Collections.emptyList();
        }
        List<PartEntity> entityList = ((FileChunkMapper) superMapper).findAllByFileId(fileId);
        log.debug("file chunk list has querying successful! size: {}", entityList.size());
        return modelActuator(entityList);
    }

    @Override
    public List<PartModel> queryAllByFileIds(Collection<String> fileIds) throws RestException {
        if (GeneralUtils.isEmpty(fileIds)) {
            return Collections.emptyList();
        }
        List<PartEntity> entityList = ((FileChunkMapper) superMapper).findAllByFileIds(fileIds);
        log.debug("file chunk list has querying successful! size: {}", entityList.size());
        return modelActuator(entityList);
    }

    @Override
    public String queryWhereSql(FileFilter filter) throws RestException {
        return filter.toFileChunkSql().toTimeSql("chunk_time").toOperateSql().toIdSql().addSorts("chunk_time").toSql();
    }

    @Override
    public String deleteWhereSql(FileFilter filter) throws RestException {
        return filter.toIdSql().toSql();
    }
}
