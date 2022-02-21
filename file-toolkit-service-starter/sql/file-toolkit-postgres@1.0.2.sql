

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_index";
CREATE TABLE "public"."file_index" (
  "id" VARCHAR(64) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" VARCHAR(64) COLLATE "pg_catalog"."default",
  "account_id" VARCHAR(64) COLLATE "pg_catalog"."default",
  "original_id" VARCHAR(64) COLLATE "pg_catalog"."default",
  "original_type" INT2,
  "original_filename" VARCHAR(512) COLLATE "pg_catalog"."default",
  "filename" VARCHAR(256) COLLATE "pg_catalog"."default",
  "alias" VARCHAR(256) COLLATE "pg_catalog"."default",
  "suffix" VARCHAR(32) COLLATE "pg_catalog"."default",
  "file_md5" VARCHAR(2048) COLLATE "pg_catalog"."default",
  "file_action" INT2,
  "file_size" INT8,
  "file_type" INT2,
  "is_finish" BOOLEAN DEFAULT TRUE,
  "is_condense" BOOLEAN DEFAULT FALSE,
  "is_slice" BOOLEAN DEFAULT FALSE,
  "slice_size" INT4,
  "is_merge" BOOLEAN DEFAULT TRUE,
  "endpoint" VARCHAR(512) COLLATE "pg_catalog"."default",
  "etag" VARCHAR(512) COLLATE "pg_catalog"."default",
  "version_id" VARCHAR(512) COLLATE "pg_catalog"."default",
  "headers" jsonb,
  "properties" jsonb,
  "update_time" TIMESTAMPTZ,
  "create_time" TIMESTAMPTZ,
  "is_del" INT2 DEFAULT 0
);
COMMENT ON COLUMN "public"."file_index"."id" IS '文件id';
COMMENT ON COLUMN "public"."file_index"."user_id" IS '文件存储用户id';
COMMENT ON COLUMN "public"."file_index"."account_id" IS '文件存储账户id';
COMMENT ON COLUMN "public"."file_index"."original_id" IS '文件所属来源Id';
COMMENT ON COLUMN "public"."file_index"."original_type" IS '文件所属来源类型';
COMMENT ON COLUMN "public"."file_index"."original_filename" IS '文件源名称';
COMMENT ON COLUMN "public"."file_index"."filename" IS '文件名称';
COMMENT ON COLUMN "public"."file_index"."alias" IS '文件别名';
COMMENT ON COLUMN "public"."file_index"."suffix" IS '文件扩展名';
COMMENT ON COLUMN "public"."file_index"."file_md5" IS '文件md5校验';
COMMENT ON COLUMN "public"."file_index"."file_action" IS '文件操作动作';
COMMENT ON COLUMN "public"."file_index"."file_size" IS '文件大小';
COMMENT ON COLUMN "public"."file_index"."file_type" IS '文件类型';
COMMENT ON COLUMN "public"."file_index"."is_finish" IS '文件分片上传完成状态';
COMMENT ON COLUMN "public"."file_index"."is_condense" IS '文件压缩状态，暂时只对图片生效';
COMMENT ON COLUMN "public"."file_index"."is_slice" IS '文件分片状态';
COMMENT ON COLUMN "public"."file_index"."slice_size" IS '文件分片总数';
COMMENT ON COLUMN "public"."file_index"."is_merge" IS '文件分片是否合并';
COMMENT ON COLUMN "public"."file_index"."endpoint" IS '文件minio存储节点';
COMMENT ON COLUMN "public"."file_index"."etag" IS '文件minio存储hash校验';
COMMENT ON COLUMN "public"."file_index"."version_id" IS '文件minio存储版本';
COMMENT ON COLUMN "public"."file_index"."headers" IS '文件minio存储请求头';
COMMENT ON COLUMN "public"."file_index"."properties" IS '文件附属属性';
COMMENT ON COLUMN "public"."file_index"."update_time" IS '文件更新时间';
COMMENT ON COLUMN "public"."file_index"."create_time" IS '文件创建时间';
COMMENT ON COLUMN "public"."file_index"."is_del" IS '文件删除标记';

-- ----------------------------
-- Primary Key structure for table file
-- ----------------------------
ALTER TABLE "public"."file_index" ADD CONSTRAINT "PK_FILE_INDEX_ID" PRIMARY KEY ("id");

CREATE INDEX "IDX_FILE_INDEX_USER_ID" ON "public"."file_index" USING btree (
  "user_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_INDEX_ACCOUNT_ID" ON "public"."file_index" USING btree (
  "account_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_INDEX_ORIGINAL_ID" ON "public"."file_index" USING btree (
  "original_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_INDEX_ORIGINAL_TYPE" ON "public"."file_index" USING btree (
  "original_type" "pg_catalog"."int2_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_INDEX_FILENAME" ON "public"."file_index" USING btree (
  "filename" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_INDEX_FILE_TYPE" ON "public"."file_index" USING btree (
  "file_type" "pg_catalog"."int2_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_INDEX_IS_FINISH" ON "public"."file_index" USING btree (
  "is_finish" "pg_catalog"."bool_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_INDEX_IS_CONDENSE" ON "public"."file_index" USING btree (
  "is_condense" "pg_catalog"."bool_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_INDEX_IS_SLICE" ON "public"."file_index" USING btree (
  "is_slice" "pg_catalog"."bool_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_INDEX_IS_MERGE" ON "public"."file_index" USING btree (
  "is_merge" "pg_catalog"."bool_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_INDEX_IS_DEL" ON "public"."file_index" USING btree (
  "is_del" "pg_catalog"."int2_ops" ASC NULLS LAST
);

DROP TABLE IF EXISTS "public"."file_chunk";
CREATE TABLE "public"."file_chunk" (
  "id" VARCHAR(64) COLLATE "pg_catalog"."default" NOT NULL,
  "file_index_id" VARCHAR(64) COLLATE "pg_catalog"."default" NOT NULL,
  "chunk_index" INT4 NOT NULL,
  "chunk_size" INT8,
  "chunk_start" INT8,
  "chunk_end" INT8,
  "chunk_md5" VARCHAR(1024) COLLATE "pg_catalog"."default",
  "is_last_chunk" BOOLEAN DEFAULT FALSE,
  "chunk_time" TIMESTAMPTZ,
  "start_time" TIMESTAMPTZ,
  "end_time" TIMESTAMPTZ,
  "is_del" INT2 DEFAULT 0
);

COMMENT ON COLUMN "public"."file_chunk"."id" IS '文件分片id';
COMMENT ON COLUMN "public"."file_chunk"."file_index_id" IS '文件id';
COMMENT ON COLUMN "public"."file_chunk"."chunk_index" IS '分片编号';
COMMENT ON COLUMN "public"."file_chunk"."chunk_size" IS '分片长度';
COMMENT ON COLUMN "public"."file_chunk"."chunk_start" IS '分片起始位置';
COMMENT ON COLUMN "public"."file_chunk"."chunk_end" IS '分片结束位置';
COMMENT ON COLUMN "public"."file_chunk"."chunk_md5" IS '分片md5校验';
COMMENT ON COLUMN "public"."file_chunk"."is_last_chunk" IS '是否最后一片数据 1：否，2：是';
COMMENT ON COLUMN "public"."file_chunk"."chunk_time" IS '分片上传时间';
COMMENT ON COLUMN "public"."file_chunk"."start_time" IS '第一片上传时间';
COMMENT ON COLUMN "public"."file_chunk"."end_time" IS '最后一片上传时间';

ALTER TABLE "public"."file_chunk" ADD CONSTRAINT "PK_FILE_CHUNK_ID" PRIMARY KEY ("id");

ALTER TABLE "public"."file_chunk" ADD CONSTRAINT "UK_FILE_CHUNK_FILE_INDEX_ID_CHUNK_INDEX" UNIQUE ("file_index_id", "chunk_index");

CREATE INDEX "IDX_FILE_CHUNK_FILE_INDEX_ID" ON "public"."file_chunk" USING btree (
  "file_index_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_CHUNK_CHUNK_INDEX" ON "public"."file_chunk" USING btree (
  "chunk_index" "pg_catalog"."int4_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_CHUNK_IS_LAST_CHUNK" ON "public"."file_chunk" USING btree (
  "is_last_chunk" "pg_catalog"."bool_ops" ASC NULLS LAST
);

CREATE INDEX "IDX_FILE_CHUNK_IS_DEL" ON "public"."file_chunk" USING btree (
   "is_del" "pg_catalog"."int2_ops" ASC NULLS LAST
);
