package io.github.nichetoolkit.file.service.impl;

import io.github.nichetoolkit.file.entity.FileIndexEntity;
import io.github.nichetoolkit.file.filter.FileFilter;
import io.github.nichetoolkit.file.mapper.FileIndexMapper;
import io.github.nichetoolkit.file.model.FileChunk;
import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.file.service.FileChunkService;
import io.github.nichetoolkit.file.service.FileIndexService;
import io.github.nichetoolkit.rest.RestException;
import io.github.nichetoolkit.rest.actuator.ConsumerActuator;
import io.github.nichetoolkit.rest.util.GeneralUtils;
import io.github.nichetoolkit.rice.RiceInfoService;
import io.github.nichetoolkit.rice.clazz.ClazzHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>FileIndexServiceImpl</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
@Slf4j
@Service
public class FileIndexServiceImpl extends RiceInfoService<FileIndex, FileIndexEntity, FileFilter> implements FileIndexService {

    @Autowired
    private FileChunkService fileChunkService;

    @Override
    public FileIndex queryByNameWithUploadInterrupt(String name) throws RestException {
        if (GeneralUtils.isEmpty(name) ) {
            return null;
        }
        FileIndexEntity entity = ((FileIndexMapper) supperMapper).findByNameWithUploadInterrupt(name);
        if (GeneralUtils.isNotEmpty(entity)) {
            return modelActuator(entity);
        }
        return null;
    }

    @Override
    public void finishSliceUpload(String id, Integer sliceSize) throws RestException {
        if (GeneralUtils.isEmpty(id) || GeneralUtils.isEmpty(sliceSize)) {
            return;
        }
        ((FileIndexMapper) supperMapper).finishSliceUpload(id,sliceSize);
    }

    @Override
    public String queryWhereSql(FileFilter filter) throws RestException {
        return filter.toFileIndexSql().toJsonbSql("properties").toTimeSql("create_time").toOperateSql().toIdSql().addSorts("update_time").toSql();
    }

    @Override
    public String deleteWhereSql(FileFilter filter) throws RestException {
        return filter.toIdSql().toSql();
    }

    @Override
    protected ConsumerActuator<FileIndex> updateActuator() {
        return this::optional;
    }

    @Override
    public void buildModel(FileIndexEntity entity, FileIndex model, Boolean... isLoadArray) throws RestException {
        if (GeneralUtils.isEmpty(model)) {
            return;
        }
        String fileId = entity.getId();
        List<FileChunk> fileChunks = fileChunkService.queryAllByFileId(fileId);
        buildLastChunk(model,fileChunks);
    }

    @Override
    public void buildModelList(Collection<FileIndexEntity> entityList, List<FileIndex> modelList, Boolean... isLoadArray) throws RestException {
        if (GeneralUtils.isEmpty(modelList)) {
            return;
        }
        List<String> fileIds = entityList.stream().filter(FileIndexEntity::getIsSlice).map(FileIndexEntity::getId).distinct().collect(Collectors.toList());
        if (GeneralUtils.isNotEmpty(fileIds)) {
            List<FileChunk> fileChunks = fileChunkService.queryAllByFileIds(fileIds);
            if (GeneralUtils.isNotEmpty(fileChunks)) {
                Map<String, List<FileChunk>> fileChunkMap = fileChunks.stream().collect(Collectors.groupingBy(FileChunk::getFileId));
                for (FileIndex fileIndex : modelList) {
                    if (fileIndex.getIsSlice()) {
                        String fileIndexId = fileIndex.getId();
                        List<FileChunk> fileChunkList = fileChunkMap.get(fileIndexId);
                        buildLastChunk(fileIndex,fileChunkList);
                    }
                }
            }
        }
    }

    private void buildLastChunk(FileIndex fileIndex,List<FileChunk> fileChunkList) {
        if (GeneralUtils.isNotEmpty(fileChunkList)) {
            Collections.sort(fileChunkList);
            fileIndex.setFileChunks(fileChunkList);
            FileChunk fileChunk = fileChunkList.get(fileChunkList.size() - 1);
            fileIndex.setFileChunk(fileChunk);
            fileIndex.setCurrentIndex(fileChunk.getChunkIndex());
        }
    }
}
