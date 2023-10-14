package co.empresa.dentalsoft.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.*;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {
	
	Cloudinary cloud;
	
	private Map<String, String> valuesMap = new HashMap<>();
	
	public CloudinaryService(){
		valuesMap.put("cloud_name", "dhvontjav");
		valuesMap.put("api_key", "421563774797412");
		valuesMap.put("api_secret", "OyKCe16hUENfbpP_JsEiPsF254Y");
		cloud = new Cloudinary(valuesMap);
	}
	
	public Map upload(MultipartFile multipartFile) throws IOException{
		File file = convert(multipartFile);
		Map result = cloud.uploader().upload(file, ObjectUtils.emptyMap());
		file.delete();
		return result;
	}
	
	public Map delete(String id) throws IOException{
		Map result = cloud.uploader().destroy(id, ObjectUtils.emptyMap());
		return result;
	}
	
	public void getImage(String url) throws Exception {
		ApiResponse resources = cloud.api().resources(ObjectUtils.asMap("type","upload"));
		List<Map> resourceList = (List<Map>) resources.get("resources");
		String publicId = null;

		for (Map resource : resourceList) {
		    String ruta = (String) resource.get("url");
		    if (ruta.equals(url)) {
		        publicId = (String) resource.get("public_id");
		        break;
		    }
		}
		
		this.delete(publicId);
	}
	
	private File convert(MultipartFile multipartFile) throws IOException {
		File file = new File(multipartFile.getOriginalFilename());
		FileOutputStream fo = new FileOutputStream(file);
		fo.write(multipartFile.getBytes());
		fo.close();
		return file;
	}

}
