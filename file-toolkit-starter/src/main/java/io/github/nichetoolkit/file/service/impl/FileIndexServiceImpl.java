package io.github.nichetoolkit.file.service;

import io.github.nichetoolkit.file.BulkEntity;
import io.github.nichetoolkit.file.FileFilter;
import io.github.nichetoolkit.file.mapper.FileIndexMapper;
import io.github.nichetoolkit.file.PartModel;
import io.github.nichetoolkit.file.BulkModel;
import io.github.nichetoolkit.rest.RestException;
import io.github.nichetoolkit.rest.actuator.BiConsumerActuator;
import io.github.nichetoolkit.rest.util.GeneralUtils;
import io.github.nichetoolkit.rice.RiceInfoService;
import io.github.nichetoolkit.rice.clazz.ClazzHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FileIndexServiceImpl extends RiceInfoService<BulkModel, BulkEntity, FileFilter> implements FileIndexService {

    @Autowired
    private FileChunkService fileChunkService;

    @Override
    protected Boolean isIdInvade() {
        return true;
    }

    @Override
    protected Boolean isIdExist() {
        return false;
    }

    @Override
    public BulkModel queryByNameWithUploadInterrupt(String name) throws RestException {
        if (GeneralUtils.isEmpty(name)) {
            return null;
        }
        BulkEntity entity = ((FileIndexMapper) superMapper).findByNameWithUploadInterrupt(name);
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
        ((FileIndexMapper) superMapper).finishSliceUpload(id, sliceSize);
    }

    @Override
    public String queryWhereSql(FileFilter filter) throws RestException {
        return filter.toFileIndexSql().toNameSql("filename").toJsonbSql("properties").toTimeSql("create_time").toOperateSql().toIdSql().addSorts("update_time").toSql();
    }

    @Override
    protected BiConsumerActuator<String, BulkModel> updateActuator() {
        return (tableKey,fileIndex) -> this.optional(fileIndex);
    }

    @Override
    public void buildModel(BulkEntity entity, BulkModel model, Boolean... isLoadArray) throws RestException {
        if (GeneralUtils.isEmpty(model)) {
            return;
        }
        String fileId = entity.getId();
        List<PartModel> fileChunks = fileChunkService.queryAllByFileId(fileId);
        buildLastChunk(model, fileChunks);
    }

    @Override
    public void buildModelList(Collection<BulkEntity> entityList, List<BulkModel> modelList, Boolean... isLoadArray) throws RestException {
        if (GeneralUtils.isEmpty(modelList)) {
            return;
        }
        List<String> fileIds = entityList.stream().filter(BulkEntity::getIsSlice).map(BulkEntity::getId).distinct().collect(Collectors.toList());
        if (GeneralUtils.isNotEmpty(fileIds)) {
            List<PartModel> fileChunks = fileChunkService.queryAllByFileIds(fileIds);
            if (GeneralUtils.isNotEmpty(fileChunks)) {
                Map<String, List<PartModel>> fileChunkMap = fileChunks.stream().collect(Collectors.groupingBy(PartModel::getFileId));
                for (BulkModel fileIndex : modelList) {
                    if (fileIndex.getIsSlice()) {
                        String fileIndexId = fileIndex.getId();
                        List<PartModel> fileChunkList = fileChunkMap.get(fileIndexId);
                        buildLastChunk(fileIndex, fileChunkList);
                    }
                }
            }
        }
    }

    private void buildLastChunk(BulkModel fileIndex, List<PartModel> fileChunkList) {
        if (GeneralUtils.isNotEmpty(fileChunkList)) {
            Collections.sort(fileChunkList);
            fileIndex.setFileChunks(fileChunkList);
            PartModel fileChunk = fileChunkList.get(fileChunkList.size() - 1);
            fileIndex.setFileChunk(fileChunk);
            fileIndex.setCurrentIndex(fileChunk.getChunkIndex());
        }
    }
}
