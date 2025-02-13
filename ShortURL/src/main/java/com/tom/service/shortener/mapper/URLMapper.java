package com.tom.service.shortener.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import com.tom.service.shortener.model.URL;
import com.tom.service.shortener.request.URLComplete;
import com.tom.service.shortener.request.URLObject;
import com.tom.service.shortener.request.URLResponse;

@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface URLMapper {
	URLMapper INSTANCE = Mappers.getMapper(URLMapper.class);

	@Mapping(source = "shortUrl", target = "shortUrl")
	@Mapping(source = "originalUrl", target = "originalUrl")
	@Mapping(source = "expirationTime", target = "expirationTime")
	URL buildAtributes(String shortUrl, String originalUrl, LocalDateTime expirationTime);

	@Mapping(source = "originalUrl", target = "originalURL")
	URLObject toUrlObject(URL url);
	
	@Mapping(source = "shortUrl", target = "shortUrl")
	@Mapping(source = "originalUrl", target = "originalUrl")
	@Mapping(source = "expirationTime", target = "dataExpiracao")
	URLResponse toUrlResponse(URL url);

	@Mapping(source = "originalUrl", target = "originalUrl")
	@Mapping(source = "shortUrl", target = "shortUrl")
	@Mapping(source = "dateCreated", target = "dataCriacao")
	@Mapping(source = "expirationTime", target = "dataExpiracao")
	@Mapping(source = "lastAccessTime", target = "dataUltimoAcesso")
	@Mapping(source = "accessCount", target = "quantidadeAcessos")
	URLComplete toUrlInfo(URL url);

}
