package io.github.nichetoolkit.file.filter;

import io.github.nichetoolkit.file.enums.FileType;
import io.github.nichetoolkit.rest.util.GeneralUtils;
import io.github.nichetoolkit.rice.RiceFilter;
import io.github.nichetoolkit.rice.builder.SqlBuilders;

/**
 * <p>FileFilter</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public class FileFilter extends RiceFilter {
    protected String userId;
    protected String suffix;
    protected Integer fileType;
    protected Boolean isSlice;
    protected Boolean isMerge;

    protected String fileId;
    protected Boolean isLastChunk;

    protected boolean isPreview = true;
    protected boolean isOriginal = true;
    protected boolean isDelete = false;
    protected boolean isChunk = false;

    public FileFilter() {
    }

    public FileFilter(String id) {
        super(id);
    }

    public FileFilter(String... ids) {
        super(ids);
    }

    public FileFilter(FileFilter.Builder builder) {
        super(builder);
        this.userId = builder.userId;
        this.suffix = builder.suffix;
        this.fileType = builder.fileType;
        this.isSlice = builder.isSlice;
        this.isMerge = builder.isMerge;
        this.fileId = builder.fileId;
        this.isLastChunk = builder.isLastChunk;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public FileType getFileType() {
        if (GeneralUtils.isEmpty(fileType)) {
            return null;
        }
        return FileType.parseKey(this.fileType);
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Boolean getSlice() {
        return isSlice;
    }

    public void setSlice(Boolean slice) {
        isSlice = slice;
    }

    public Boolean getMerge() {
        return isMerge;
    }

    public void setMerge(Boolean merge) {
        isMerge = merge;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public void setPreview(boolean preview) {
        isPreview = preview;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    public boolean isChunk() {
        return isChunk;
    }

    public void setChunk(boolean chunk) {
        isChunk = chunk;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Boolean getLastChunk() {
        return isLastChunk;
    }

    public void setLastChunk(Boolean lastChunk) {
        isLastChunk = lastChunk;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public FileFilter toFileChunkSql() {
        if (GeneralUtils.isNotEmpty(this.fileId)) {
            SqlBuilders.equal(this.SQL_BUILDER, "file_index_id", this.fileId);
        }
        if (GeneralUtils.isNotEmpty(this.isLastChunk)) {
            SqlBuilders.equal(this.SQL_BUILDER, "is_last_chunk", this.isLastChunk);
        }
        return this;
    }

    public FileFilter toFileIndexSql() {
        if (GeneralUtils.isNotEmpty(this.userId)) {
            SqlBuilders.equal(this.SQL_BUILDER, "user_id", this.userId);
        }
        if (GeneralUtils.isNotEmpty(this.suffix)) {
            SqlBuilders.equal(this.SQL_BUILDER, "suffix", this.suffix);
        }
        if (GeneralUtils.isNotEmpty(this.fileType)) {
            SqlBuilders.equal(this.SQL_BUILDER, "file_type", String.valueOf(this.fileType));
        }
        if (GeneralUtils.isNotEmpty(this.isSlice)) {
            SqlBuilders.equal(this.SQL_BUILDER, "is_slice", this.isSlice);
        }
        if (GeneralUtils.isNotEmpty(this.isMerge)) {
            SqlBuilders.equal(this.SQL_BUILDER, "is_merge", this.isMerge);
        }
        return this;
    }

    public static class Builder extends RiceFilter.Builder {
        protected String userId;
        protected String suffix;
        protected Integer fileType;
        protected boolean isSlice;
        protected boolean isMerge;
        protected String fileId;
        protected boolean isLastChunk;

        public Builder() {
        }

        public FileFilter.Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public FileFilter.Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public FileFilter.Builder fileType(FileType fileType) {
            this.fileType = fileType.getKey();
            return this;
        }


        public FileFilter.Builder fileType(Integer fileType) {
            this.fileType = fileType;
            return this;
        }

        public FileFilter.Builder isSlice(boolean isSlice) {
            this.isSlice = isSlice;
            return this;
        }

        public FileFilter.Builder isMerge(boolean isMerge) {
            this.isMerge = isMerge;
            return this;
        }

        public FileFilter.Builder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public FileFilter.Builder isLastChunk(boolean isLastChunk) {
            this.isLastChunk = isLastChunk;
            return this;
        }

        public FileFilter build() {
            return new FileFilter(this);
        }
    }


}
