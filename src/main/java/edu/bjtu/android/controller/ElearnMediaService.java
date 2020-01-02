package edu.bjtu.android.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.bjtu.android.dao.CourseDao;
import edu.bjtu.android.dao.MaterialDao;
import edu.bjtu.android.dao.TeacherDao;
import edu.bjtu.android.entity.Course;
import edu.bjtu.android.entity.Material;
import edu.bjtu.android.entity.Teacher;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;


@RestController
@RequestMapping("/elearn")
public class ElearnMediaService {
    final long ChunkSize = 1048576L;
	@Autowired
	CourseDao courseDao;
	
	@Autowired
	MaterialDao materialDao;
	
	@Autowired
	TeacherDao teacherDao;

	@Value("${material.location}")
	private String contentPath;
	
	@RequestMapping(method = RequestMethod.GET, path = "/documents/{name}")
	public ResponseEntity<UrlResource> getFile(@PathVariable("name") String name, @RequestHeader HttpHeaders headers) throws Exception {
		
		//List<Material> materials = materialDao.selectByCourseId(courseid);
		String path = new File("").getAbsolutePath();
		String vpath = "file:///" + path +"\\"+ contentPath+"\\"+name;
		UrlResource doc = new UrlResource(vpath);
        return ResponseEntity.ok()
        		.contentType(MediaTypeFactory.getMediaType(doc).orElse(MediaType.APPLICATION_OCTET_STREAM))
        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFilename() + "\"")
        		.body(doc);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/images/{name}")
	public ResponseEntity<UrlResource> getImage(@PathVariable("name") String name, @RequestHeader HttpHeaders headers) throws Exception {
		
		//List<Material> materials = materialDao.selectByCourseId(courseid);
		String path = new File("").getAbsolutePath();
		String vpath = "file:///" + path +"\\"+ contentPath+"\\"+name;
		UrlResource doc = new UrlResource(vpath);
        return ResponseEntity.ok()
        		.contentType(MediaTypeFactory.getMediaType(doc).orElse(MediaType.APPLICATION_OCTET_STREAM))
        		.body(doc);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/videos/{name}")
	public ResponseEntity<ResourceRegion> getVideo(@PathVariable("name") String name, @RequestHeader HttpHeaders headers) throws Exception {
		
		//List<Material> materials = materialDao.selectByCourseId(courseid);
//	    try {
//	    	UrlResource resource = new UrlResource(videoPath+"/"+name);
//	        if(resource.exists() || resource.isReadable()) {
//	            //return resource;
//	        }
//	        else {
//	            throw new RuntimeException("Could not read file: " + name);
//
//	        }
//	    } catch (MalformedURLException e) {
//	        throw new RuntimeException("Could not read file: " + name, e);
//	    }
		String path = new File("").getAbsolutePath();
		String vpath = "file:///" + path +"\\"+ contentPath+"\\"+name;
		UrlResource video = new UrlResource(vpath);
		ResourceRegion region = resourceRegion(video, headers);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(region);
	}

	
    private ResourceRegion resourceRegion(UrlResource video, HttpHeaders headers) throws Exception {
        long contentLength = video.contentLength();
        //long range = headers.range.firstOrNull();
        //long range = headers.getRange().isEmpty()
        
       HttpRange range;
       if (!headers.getRange().isEmpty()) {
    	   
    	    range = headers.getRange().get(0);
    	   
            long start = range.getRangeStart(contentLength);
            long end = range.getRangeEnd(contentLength);
            long size = end - start + 1;
            long rangeLength = ChunkSize>size? size:ChunkSize;
            return new ResourceRegion(video, start, rangeLength);
        } else {
            long rangeLength = ChunkSize>contentLength?contentLength:ChunkSize;
            return new ResourceRegion(video, 0, rangeLength);
        }
    }

	@RequestMapping(method = RequestMethod.GET, path = "/materials/{mid}/media")
	public ResponseEntity<ResourceRegion> getMaterialMedia(@PathVariable("mid") String mid, @RequestHeader HttpHeaders headers) throws Exception {
        Material material = materialDao.selectByPrimaryKey(mid);
        return getVideo(material.getMaterialUrl(), headers);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/materials/{mid}/file")
	public ResponseEntity<UrlResource> getMaterialDoc(@PathVariable("mid") String mid, @RequestHeader HttpHeaders headers) throws Exception {
        Material material = materialDao.selectByPrimaryKey(mid);
  System.out.println("mid: "+ mid);
		System.out.println("create date: "+ material.getCreateDate());
		switch (material.getMediatype().toLowerCase()) {
        case "video":
        	return getFile(material.getMaterialUrl(), headers);
		case "audio":
        	return getFile(material.getMaterialUrl(), headers);

        case "image":
        	return getImage(material.getMaterialUrl(), headers);
        	
        default:
        	return getFile(material.getMaterialUrl(), headers);        	
        	
        }	
        	
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/materials/{mid}/videoframe")
	public ResponseEntity<UrlResource> getMaterialVideoframe(@PathVariable("mid") String mid, @RequestHeader HttpHeaders headers) throws Exception {
        Material material = materialDao.selectByPrimaryKey(mid);
  
		switch (material.getMediatype().toLowerCase()) {
	        case "video":
	        	String vframe = getVideoframe(material.getMaterialUrl());
	        	return getImage(vframe, headers);
			case "audio":
	        	return getFile(material.getMaterialUrl(), headers);
	
	        case "image":
	        	return getImage(material.getMaterialUrl(), headers);
	        	
	        default:
	        	return getFile(material.getMaterialUrl(), headers);        	
	        	
	        }	
        	
	 }
	
	private String getVideoframe(String videopath) throws IOException, JCodecException {
		String vframe;
		
		int frameNumber = 2;
		
		int i =videopath.lastIndexOf('.');
		vframe = videopath.substring(0,i)+".jpg";
		
		File f = new File(vframe);
		if (!f.exists())
		{
			String path = new File("").getAbsolutePath();
			videopath = path +"\\"+ contentPath+"\\"+ videopath;
			Picture picture = FrameGrab.getFrameFromFile(new File(videopath), frameNumber);
	
			//for JDK (jcodec-javase)
			BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
			ImageIO.write(bufferedImage, "jpg", new File(path +"\\"+ contentPath+"\\"+ vframe));
		}
		return vframe;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/teachers/{tid}/photo")
	public ResponseEntity<UrlResource> getTeacherPhoto(@PathVariable("tid") String tid, @RequestHeader HttpHeaders headers) throws Exception {
        Teacher teacher = teacherDao.selectByPrimaryKey(tid); 
        return getImage(teacher.getPhoto(), headers);       	
	}

	@RequestMapping(method = RequestMethod.GET, path = "/courses/{cid}/photo")
	public ResponseEntity<UrlResource> getCourseAvatar(@PathVariable("cid") String cid, @RequestHeader HttpHeaders headers) throws Exception {
        Course course = courseDao.selectByPrimaryKey(cid); 
        return getImage(course.getAvatar(), headers);       	
	}


}
