
DROP TABLE IF EXISTS "public"."file_bulk";
CREATE TABLE "public"."file_bulk" (
    "id"           VARCHAR(64) COLLATE "pg_catalog"."default" NOT NULL,
    "user"         VARCHAR(64) COLLATE "pg_catalog"."default",
    "original"     VARCHAR(512) COLLATE "pg_catalog"."default",
    "filename"     VARCHAR(256) COLLATE "pg_catalog"."default",
    "alias"        VARCHAR(256) COLLATE "pg_catalog"."default",
    "suffix"       VARCHAR(32) COLLATE "pg_catalog"."default",
    "file_md5"     VARCHAR(256) COLLATE "pg_catalog"."default",
    "file_size"    INT8,
    "file_type"    INT2,
    "part_state"   BOOLEAN DEFAULT FALSE,
    "part_size"    INT4,
    "finish_state" BOOLEAN DEFAULT TRUE,
    "zip_state"    BOOLEAN DEFAULT FALSE,
    "merge_state"  BOOLEAN DEFAULT TRUE,
    "etag"         VARCHAR(512) COLLATE "pg_catalog"."default",
    "operate"      INT4,
    "version"      VARCHAR(512) COLLATE "pg_catalog"."default",
    "headers"      jsonb,
    "properties"   jsonb,
    "logic" INT4,
    "update_time"  TIMESTAMPTZ,
    "create_time"  TIMESTAMPTZ
);

-- ----------------------------
-- Primary Key structure for table file
-- ----------------------------
ALTER TABLE "public"."file_bulk"
    ADD CONSTRAINT "PK_file_bulk_ID"
        PRIMARY KEY ("id");

CREATE INDEX "IDX_file_bulk_USER_ID" ON "public"."file_bulk" USING btree ( "user_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST );

CREATE INDEX "IDX_file_bulk_FILENAME" ON "public"."file_bulk" USING btree ( "filename" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST );

CREATE INDEX "IDX_file_bulk_FILE_TYPE" ON "public"."file_bulk" USING btree ( "file_type" "pg_catalog"."int2_ops" ASC NULLS LAST );

CREATE INDEX "IDX_file_bulk_IS_FINISH" ON "public"."file_bulk" USING btree ( "is_finish" "pg_catalog"."bool_ops" ASC NULLS LAST );

CREATE INDEX "IDX_file_bulk_IS_CONDENSE" ON "public"."file_bulk" USING btree ( "is_condense" "pg_catalog"."bool_ops" ASC NULLS LAST );

CREATE INDEX "IDX_file_bulk_IS_SLICE" ON "public"."file_bulk" USING btree ( "is_slice" "pg_catalog"."bool_ops" ASC NULLS LAST );

CREATE INDEX "IDX_file_bulk_IS_MERGE" ON "public"."file_bulk" USING btree ( "is_merge" "pg_catalog"."bool_ops" ASC NULLS LAST );

CREATE INDEX "IDX_file_bulk_OPERATE" ON "public"."file_bulk" USING btree ( "operate" "pg_catalog"."int4_ops" ASC NULLS LAST );

DROP TABLE IF EXISTS "public"."file_chunk";
CREATE TABLE "public"."file_chunk" (
    "id"            VARCHAR(64) COLLATE "pg_catalog"."default" NOT NULL,
    "file_bulk_id" VARCHAR(64) COLLATE "pg_catalog"."default" NOT NULL,
    "chunk_index"   INT4                                       NOT NULL,
    "chunk_size"    INT8,
    "chunk_start"   INT8,
    "chunk_end"     INT8,
    "chunk_md5"     VARCHAR(1024) COLLATE "pg_catalog"."default",
    "is_last_chunk" BOOLEAN DEFAULT FALSE,
    "chunk_time"    TIMESTAMPTZ,
    "start_time"    TIMESTAMPTZ,
    "end_time"      TIMESTAMPTZ,
    "operate"       INT4
);

COMMENT
ON COLUMN "public"."file_chunk"."id" IS '文件分片id';
COMMENT
ON COLUMN "public"."file_chunk"."file_bulk_id" IS '文件id';
COMMENT
ON COLUMN "public"."file_chunk"."chunk_index" IS '分片编号';
COMMENT
ON COLUMN "public"."file_chunk"."chunk_size" IS '分片长度';
COMMENT
ON COLUMN "public"."file_chunk"."chunk_start" IS '分片起始位置';
COMMENT
ON COLUMN "public"."file_chunk"."chunk_end" IS '分片结束位置';
COMMENT
ON COLUMN "public"."file_chunk"."chunk_md5" IS '分片md5校验';
COMMENT
ON COLUMN "public"."file_chunk"."is_last_chunk" IS '是否最后一片数据 1：否，2：是';
COMMENT
ON COLUMN "public"."file_chunk"."chunk_time" IS '分片上传时间';
COMMENT
ON COLUMN "public"."file_chunk"."start_time" IS '第一片上传时间';
COMMENT
ON COLUMN "public"."file_chunk"."end_time" IS '最后一片上传时间';
COMMENT
ON COLUMN "public"."file_chunk"."operate" IS '文件操作';

ALTER TABLE "public"."file_chunk"
    ADD CONSTRAINT "PK_FILE_CHUNK_ID"
        PRIMARY KEY ("id");

ALTER TABLE "public"."file_chunk"
    ADD CONSTRAINT "UK_FILE_CHUNK_file_bulk_ID_CHUNK_INDEX"
        UNIQUE ("file_bulk_id", "chunk_index");

CREATE INDEX "IDX_FILE_CHUNK_file_bulk_ID" ON "public"."file_chunk" USING btree ( "file_bulk_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST );

CREATE INDEX "IDX_FILE_CHUNK_CHUNK_INDEX" ON "public"."file_chunk" USING btree ( "chunk_index" "pg_catalog"."int4_ops" ASC NULLS LAST );

CREATE INDEX "IDX_FILE_CHUNK_OPERATE" ON "public"."file_chunk" USING btree ( "operate" "pg_catalog"."int4_ops" ASC NULLS LAST );