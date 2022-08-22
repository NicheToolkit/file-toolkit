package io.github.nichetoolkit.file.controller;


import io.github.nichetoolkit.file.configure.FileCommonProperties;
import io.github.nichetoolkit.file.constant.FileConstants;
import io.github.nichetoolkit.file.error.FileErrorStatus;
import io.github.nichetoolkit.file.filter.FileFilter;
import io.github.nichetoolkit.file.helper.FileServiceHelper;
import io.github.nichetoolkit.file.model.FileIndex;
import io.github.nichetoolkit.file.model.FileRequest;
import io.github.nichetoolkit.file.service.FileService;
import io.github.nichetoolkit.rest.RestException;
import io.github.nichetoolkit.rest.RestResult;
import io.github.nichetoolkit.rest.error.natives.ServiceErrorException;
import io.github.nichetoolkit.rest.util.FileUtils;
import io.github.nichetoolkit.rest.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileCommonProperties commonProperties;

    @Autowired
    private FileService fileService;


    @GetMapping("/img/download")
    public void imgDownload(@RequestParam(value = "fileId") String fileId,
                            @RequestParam(value = "preview", required = false, defaultValue = "true") Boolean preview,
                            @RequestParam(value = "original", required = false, defaultValue = "false") Boolean original,
                            HttpServletRequest request, HttpServletResponse response) throws RestException {
        log.info("the image will be started downloading with file Id: {}", fileId);
        fileService.imageDownload(fileId, preview, original, request, response);
    }

    @GetMapping("/file/download")
    public void fileDownload(@RequestParam(value = "fileId") String fileId,
                             @RequestParam(value = "chunk", required = false, defaultValue = "false") Boolean chunk,
                             @RequestParam(value = "preview", required = false, defaultValue = "true") Boolean preview,
                             @RequestParam(value = "original", required = false, defaultValue = "true") Boolean original,
                             HttpServletRequest request, HttpServletResponse response) throws RestException  {
        log.info("the file will be started downloading with file Id: {}", fileId);
        fileService.fileDownload(fileId, chunk, preview, original, request, response);
    }

    @Deprecated
    @GetMapping("/download")
    public void download(@RequestParam(value = "fileId") String fileId,
                         @RequestParam(value = "chunk", required = false, defaultValue = "false") Boolean chunk,
                         @RequestParam(value = "preview", required = false, defaultValue = "true") Boolean preview,
                         @RequestParam(value = "original", required = false, defaultValue = "true") Boolean original,
                         HttpServletRequest request, HttpServletResponse response) throws RestException  {
        log.info("the file will be started downloading with file Id: {}", fileId);
        fileService.fileDownload(fileId, chunk, preview, original, request,response);
    }

    @GetMapping("/filter/download")
    public void download(FileFilter fileFilter, HttpServletRequest request, HttpServletResponse response) throws RestException  {
        log.info("the file will be started downloading with file filter: {}", JsonUtils.parseJson(fileFilter));
        fileService.download(fileFilter, request, response);
    }

    @PostMapping("/file/upload")
    public ResponseEntity<FileIndex> fileUpload(@NonNull @RequestPart("file") MultipartFile file, FileRequest fileRequest) throws RestException  {
        String originalFilename = file.getOriginalFilename();
        log.info("the file will be started uploading at 'fileUpload', filename: {}", originalFilename);
        FileIndex fileUpload = fileService.upload(file, fileRequest);
        return ResponseEntity.ok(fileUpload);
    }

    @PostMapping("/image/upload")
    public ResponseEntity<FileIndex> imageUpload(@NonNull @RequestPart("file") MultipartFile file, FileRequest fileRequest) throws RestException  {
        String originalFilename = file.getOriginalFilename();
        log.info("the image file will be started uploading at 'imageUpload', filename: {}", originalFilename);
        FileIndex fileUpload = fileService.upload(file, fileRequest);
        return ResponseEntity.ok(fileUpload);
    }

    @PostMapping("/image/autograph")
    public ResponseEntity<FileIndex> imageAutograph(@NonNull @RequestPart("file") MultipartFile file, FileRequest fileRequest) throws RestException  {
        String originalFilename = file.getOriginalFilename();
        fileRequest.setIsAutograph(true);
        log.info("the image file will be started uploading at 'imageAutograph', filename: {}", originalFilename);
        FileIndex fileUpload = fileService.upload(file, fileRequest);
        return ResponseEntity.ok(fileUpload);
    }

    @PostMapping("/index/upload")
    public ResponseEntity<FileIndex> indexUpload(@NonNull @RequestBody FileIndex fileIndex) throws RestException  {
        String originalFilename = fileIndex.getName();
        log.info("the index file will be started uploading at 'indexUpload', filename: {}", originalFilename);
        FileIndex createIndex = FileServiceHelper.createFileIndex(fileIndex);
        FileIndex fileUpload = fileService.indexUpload(createIndex);
        return ResponseEntity.ok(fileUpload);
    }

    @PostMapping("/chunk/upload")
    public ResponseEntity chunkUpload(@NonNull @RequestPart("file") MultipartFile file,
                                               @RequestHeader(value = FileConstants.CONTENT_RANGE_HEADER) String contentRange,
                                               FileRequest fileRequest) throws RestException  {
        String originalFilename = file.getOriginalFilename();
        log.info("the chunk file will be started uploading at 'chunkUpload', filename: {}", originalFilename);
        Future<FileIndex> indexFuture = fileService.chunkUpload(file, contentRange, fileRequest);
        try {
            return RestResult.ok(indexFuture.get());
        } catch (InterruptedException | ExecutionException exception) {
            throw new ServiceErrorException(FileErrorStatus.SERVICE_DOWNLOAD_ERROR,exception.getMessage());
        }
    }

    @Deprecated
    @PostMapping("/upload")
    public ResponseEntity upload(@NonNull @RequestPart("file") MultipartFile file, FileRequest fileRequest) throws RestException {
        String originalFilename = file.getOriginalFilename();
        log.info("the file will be started uploading at 'upload', filename: {}", originalFilename);
        FileIndex fileUpload = fileService.upload(file, fileRequest);
        return RestResult.ok(fileUpload);
    }


    @PostMapping("/img/remove")
    public ResponseEntity imgRemove(@RequestParam(value = "fileId") String fileId,
                                  @RequestParam(value = "delete", required = false, defaultValue = "false") Boolean delete,
                                  @RequestParam(value = "rename", required = false, defaultValue = "false") Boolean rename) throws RestException  {
        log.info("the image will be started removing with file Id: {}", fileId);
        fileService.remove(fileId, false, delete, rename);
        return RestResult.ok();
    }

    @PostMapping("/file/remove")
    public ResponseEntity fileRemove(@RequestParam(value = "fileId") String fileId,
                                   @RequestParam(value = "chunk", required = false, defaultValue = "false") Boolean chunk,
                                   @RequestParam(value = "delete", required = false, defaultValue = "false") Boolean delete,
                                   @RequestParam(value = "rename", required = false, defaultValue = "true") Boolean rename) throws RestException  {
        log.info("the file will be started removing with file Id: {}", fileId);
        fileService.remove(fileId,  chunk, delete, rename);
        return RestResult.ok();
    }

    @PostMapping("/filter/remove")
    public ResponseEntity filterRemove(FileFilter fileFilter) throws RestException  {
        log.info("the file will be started removing with file filter: {}", JsonUtils.parseJson(fileFilter));
        fileService.remove(fileFilter);
        return RestResult.ok();
    }

    @PostMapping("/slice")
    public ResponseEntity slice(@NonNull @RequestPart("file") MultipartFile file) throws RestException  {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        log.info("the file will be started slicing with filename: {}", originalFilename);
        String filename = FileUtils.filename(originalFilename);
        String tempPath = commonProperties.getTempPath();
        try {
            String cachePath = FileUtils.createPath(tempPath, filename);
            File originalFile = FileUtils.createFile(cachePath.concat(originalFilename));
            file.transferTo(originalFile);
            long chunkSize = 200 * 1024 * 1024L;
            long sliceSize = originalFile.length() / chunkSize;
            if (file.getSize() % chunkSize != 0) {
                sliceSize += 1;
            }
            RandomAccessFile accessFile = new RandomAccessFile(originalFile, "rwd");
            FileChannel accessFileChannel = accessFile.getChannel();
            for (int i = 1; i <= sliceSize; i++) {
                long position = (i - 1) * chunkSize;
                long positionEnd = position + chunkSize;
                if (i == sliceSize) {
                    positionEnd = file.getSize();
                }
                chunkSize = positionEnd - position;
                String sliceFilename = cachePath.concat(File.separator).concat(filename).concat("_").concat(String.valueOf(i))
                        .concat("_").concat(String.valueOf(position))
                        .concat("_").concat(String.valueOf(positionEnd))
                        .concat("_").concat(String.valueOf(chunkSize))
                        .concat("_").concat(String.valueOf(file.getSize()));
                File sliceFile = FileUtils.createFile(sliceFilename);
                RandomAccessFile sliceAccessFile = new RandomAccessFile(sliceFile, "rwd");
                FileChannel sliceAccessFileChannel = sliceAccessFile.getChannel();
                long transferTo = accessFileChannel.transferTo(position, chunkSize, sliceAccessFileChannel);
                sliceAccessFileChannel.close();
            }
            accessFileChannel.close();
        } catch (IOException exception) {
            throw new ServiceErrorException(exception.getMessage());
        }
        return RestResult.ok();
    }

}

