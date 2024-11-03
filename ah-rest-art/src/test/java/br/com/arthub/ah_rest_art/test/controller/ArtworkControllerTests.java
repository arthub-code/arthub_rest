package br.com.arthub.ah_rest_art.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.fail;

import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import br.com.arthub.ah_rest_art.constants.ArtImageReferenceUploadType;
import br.com.arthub.ah_rest_art.dto.ArtImageReferencePayload;
import br.com.arthub.ah_rest_art.dto.FileData;
import br.com.arthub.ah_rest_art.test.utils.AuthTokenUtil;
import br.com.arthub.ah_rest_art.utils.MultipartFileUtils;

public class ArtworkControllerTests {

    private AuthTokenUtil authUtil = new AuthTokenUtil();
    private JSONObject firstArt;
    private JSONArray array;
    
    private static boolean bLog = false;
    
    @BeforeEach
    void before() throws Exception {
    	// garantir que o atributo firstArt sempre esteja preenchido
    	testGetArts();
    }

    @Test
    @DisplayName("Teste de sucesso da criação de uma arte sem data de prazo no sistema do arthub")
    void testDoCreateAnArt01() throws Exception {
        String payloadJson = """
        {
            "artName": "My Art!",
            "haveSchedule": false,
            "artImageRef": [
                {
                    "uploadType": "PINTEREST_API",
                    "imageLink": "https://i.pinimg.com/564x/00/73/13/007313698e90b12f40532d6ea72fff10.jpg"
                }
            ]
        }
        """;
        
        testCreateArt(payloadJson);
    }
    
    @Test
    @DisplayName("Teste de sucesso de criação de arte com data de prazo no sistema do arthub")
    void testDoCreateAnArt02() throws Exception {
    	String payloadJson = String.format("""
        {
            "artName": "My Art!",
            "haveSchedule": true,
            "startScheduleDate": "%s",
            "endScheduleDate": "%s",
            "artImageRef": [
            
                {
                    "uploadType": "PINTEREST_API",
                    "imageLink": "https://i.pinimg.com/564x/00/73/13/007313698e90b12f40532d6ea72fff10.jpg"
                }
            ]
        }
        """, LocalDate.now().toString(), LocalDate.now().plusDays(1).toString());
    	testCreateArt(payloadJson);
    }
    
    @Test
    @DisplayName("Teste de sucesso de criação de arte com imagens de referência em arquivo.")
    void testDoCreateAnArt03() throws Exception {
    	FileData f = makeFileData("test.png");
    	String payloadJson = String.format("""
    	    {
    	        "artName": "My Art!",
    	        "haveSchedule": true,
    	        "startScheduleDate": "%s",
    	        "endScheduleDate": "%s",
    	        "artImageRef": [
    	            {
    	                "uploadType": "DEVICE_UPLOAD",
    	                "fileData": {
    	                    "base64": "%s",
    	                    "fileName": "%s",
    	                    "contentType": "%s"
    	                },
    	                "imageLink": null
    	            }
    	        ]
    	    }
    	    """, LocalDate.now().toString(), LocalDate.now().plusDays(1).toString(), f.getBase64(), f.getFileName(), f.getContentType());
    	testCreateArt(payloadJson);
    }
    
    @Test
    @DisplayName("Teste de falha de criação de uma arte com o upload type errado enviando um arquivo imagem")
    void testDoCreateAnArt04() throws Exception {
    	FileData f = makeFileData("test.png");
    	String payloadJson = String.format("""
    	    {
    	        "artName": "My Art!",
    	        "haveSchedule": true,
    	        "startScheduleDate": "%s",
    	        "endScheduleDate": "%s",
    	        "artImageRef": [
    	            {
    	                "uploadType": "DEVICE_UPLOAD",
    	                "fileData": {
    	                    "base64": "%s",
    	                    "fileName": "%s",
    	                    "contentType": "%s"
    	                },
    	                "imageLink": "https://i.pinimg.com/564x/00/73/13/007313698e90b12f40532d6ea72fff10.jpg"
    	            }
    	        ]
    	    }
    	    """, LocalDate.now().toString(), LocalDate.now().plusDays(1).toString(), f.getBase64(), f.getFileName(), f.getContentType());
    	testCreateArt(payloadJson, 400, true);
    }
    
    @Test
    @DisplayName("Teste de falha de criação de uma arte com o upload type errado enviando uma imagem externa")
    void testDoCreateAnArt05() throws Exception {
    	FileData f = makeFileData("test.png");
    	String payloadJson = String.format("""
    	    {
    	        "artName": "My Art!",
    	        "haveSchedule": true,
    	        "startScheduleDate": "%s",
    	        "endScheduleDate": "%s",
    	        "artImageRef": [
    	            {
    	                "uploadType": "PINTEREST_API",
    	                "fileData": {
    	                    "base64": "%s",
    	                    "fileName": "%s",
    	                    "contentType": "%s"
    	                },
    	                "imageLink": "https://i.pinimg.com/564x/00/73/13/007313698e90b12f40532d6ea72fff10.jpg"
    	            }
    	        ]
    	    }
    	    """, LocalDate.now().toString(), LocalDate.now().plusDays(1).toString(), f.getBase64(), f.getFileName(), f.getContentType());
    	testCreateArt(payloadJson, 400, true);
    }
    
    @Test
    @DisplayName("Teste de falha de criação de uma arte com dados obrigatórios nulos")
    void testDoCreateAnArt06() throws Exception {
        String payloadJson = """
        {
            "artName": "",
            "haveSchedule": false,
            "artImageRef": [
                {
                    "uploadType": "PINTEREST_API",
                    "imageLink": "https://i.pinimg.com/564x/00/73/13/007313698e90b12f40532d6ea72fff10.jpg"
                }
            ]
        }
        """;
        testCreateArt(payloadJson, 400, true);
    }
    
    @Test
    @DisplayName("Teste de falha de criação de uma arte com a data inicial de prazo anterior a data atual")
    void testDoCreateAnArt07() throws Exception {
    	 String payloadJson = String.format("""
        {
            "artName": "My arte yeah",
            "haveSchedule": true,
            "startScheduleDate": "2023-11-01",
            "endScheduleDate": "%s",
            "artImageRef": [
                {
                    "uploadType": "PINTEREST_API",
                    "imageLink": "https://i.pinimg.com/564x/00/73/13/007313698e90b12f40532d6ea72fff10.jpg"
                }
            ]
        }
        """, LocalDate.now().plusDays(1).toString());
    	testCreateArt(payloadJson, 400, true);
    }
    
    @Test
    @DisplayName("Teste de falha de criação de uma arte com a data término de prazo anterior a data inicial de prazo")
    void testDoCreateAnArt08() throws Exception {
    	 String payloadJson = String.format("""
        {
            "artName": "My arte yeah",
            "haveSchedule": true,
            "startScheduleDate": "%s",
            "endScheduleDate": "2023-11-05",
            "artImageRef": [
                {
                    "uploadType": "PINTEREST_API",
                    "imageLink": "https://i.pinimg.com/564x/00/73/13/007313698e90b12f40532d6ea72fff10.jpg"
                }
            ]
        }
        """, LocalDate.now().toString());
    	testCreateArt(payloadJson, 400, true);
    }
    
    @Test
    @DisplayName("Teste de sucesso da listagem de artes registradas de um artista no sistema do arthub")
    void testAllUserArts01() throws Exception {
    	testGetArts();
    	sysout(this.firstArt.toString());
    }
    
    @Test
    @DisplayName("Teste de sucesso de detalhamento de uma arte registrada no sistema do arthub")
    void testDetailsArt01() throws Exception {
    	Res response = getArtById(UUID.fromString(this.firstArt.getString("artId")));
    	JSONObject jsonResponse = response.obj;
        assertEquals(200, response.status, "O código de status deve ser 200 para uma arte existente");
        assertEquals(false, jsonResponse.getBoolean("hasErrors"), "A resposta não deve conter erros");
       
        JSONObject data = jsonResponse.getJSONObject("data");
        
        sysout("Objeto ja guardado: " + this.firstArt);
        sysout("Teste de details: " + jsonResponse);
        assertEquals(this.firstArt.getString("artId"), data.getString("artId"), "O id da arte deve ser igual ao esperado");
    }
    
    @Test
    @DisplayName("Teste de falha de detalhamento de uma arte inexistente no sistema do arthub")
    void testDetailsArt02() throws Exception {
    	Res response = getArtById(UUID.randomUUID());
    	JSONObject jsonResponse = response.obj;
    	assertEquals(400, response.status, "O código de status deve ser 400 Bad Request para uma arte inexistente");
        assertEquals(true, jsonResponse.getBoolean("hasErrors"), "A resposta deve indicar a presença de erros");
        sysout("Resposta de erro: " + jsonResponse);
    }
    
    @Test
    @DisplayName("Teste de sucesso de deleção completa de uma arte registrada no sistema do arthub")
    void testDoFullDeleteArt01() throws Exception {
    	 JSONObject jsonResponse = getArtById(UUID.fromString(this.firstArt.getString("artId"))).obj.getJSONObject("data");
    	 int status = deleteAnArt(jsonResponse.getString("artId"));
    	 assertEquals(204, status, "O código de status deve ser 204 No Content para uma arte excluida");
    }
    
    @Test
    @DisplayName("Teste de falha de deleção completa de uma arte inexistente no sistema do arthub")
    void testDoFullDeleteArt02() throws Exception {
    	 int status = deleteAnArt(UUID.randomUUID().toString());
    	 assertEquals(400, status, "O código de status deve ser 400 Bad Request para uma arte inexistenteX");
    }

    @Test
    @DisplayName("Teste de sucesso de atualização dos dados de uma arte registrada no sistema do arthub.")
    void testDoChangeArtwork01() throws Exception {
    	String jsonPayload = String.format("""
		{
    		"artName": "Art Updated",
    		"haveSchedule": true,
    		"startScheduleDate": "%s",
    		"endScheduleDate": "%s"
		}
    	""", LocalDate.now().toString(), LocalDate.now().plusDays(1).toString());
    	Res response = updateArtworkById(this.firstArt.getString("artId"), jsonPayload);
    	JSONObject updated = getArtById(UUID.fromString(this.firstArt.getString("artId"))).obj.getJSONObject("data");
    	
    	assertEquals(200, response.status, "O código de status deve ser 200 para uma arte atualizada.");
    	assertEquals("Art Updated", updated.getString("artName"), "O nome da arte deve ser igual ao nome atualizado");
    }
    
    @Test 
    @DisplayName("Teste de falha de atualição dos dados vazios e nulos de uma arte registrada no sistema do arthub")
    void testDoChangeArtwork02() throws Exception {
    	String jsonPayload = String.format("""
		{
    		"artName": "",
    		"haveSchedule": null,
    		"startScheduleDate": "%s",
    		"endScheduleDate": "%s"
		}
    	""", LocalDate.now().toString(), LocalDate.now().plusDays(1).toString());
       	Res response = updateArtworkById(this.firstArt.getString("artId"), jsonPayload);
    	assertEquals(400, response.status, "O código de status deve ser 400 Bad Request para uma arte atualizada.");
    }
    
    @Test 
    @DisplayName("Teste de falha de atualição dos dados de uma arte inexistente no sistema do arthub")
    void testDoChangeArtwork03() throws Exception {
    	String jsonPayload = String.format("""
		{
    		"artName": "Art Updated",
    		"haveSchedule": true,
    		"startScheduleDate": "%s",
    		"endScheduleDate": "%s"
		}
    	""", LocalDate.now().toString(), LocalDate.now().plusDays(1).toString());
       	Res response = updateArtworkById(UUID.randomUUID().toString(), jsonPayload);
    	assertEquals(400, response.status, "O código de status deve ser 400 Bad Request para uma arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de sucesso de atualização dos dados de uma arte registrada desabilitando a data de prazo")
    void testDoChangeArtwork04() throws Exception {
    	String jsonPayload = String.format("""
		{
    		"artName": "Art Updated",
    		"haveSchedule": false,
    		"startScheduleDate": "%s",
    		"endScheduleDate": "%s"
		}
    	""", LocalDate.now().toString(), LocalDate.now().plusDays(1).toString());
    	Res response = updateArtworkById(this.firstArt.getString("artId"), jsonPayload);
    	JSONObject updated = getArtById(UUID.fromString(this.firstArt.getString("artId"))).obj.getJSONObject("data");
    	
    	assertEquals(200, response.status, "O código de status deve ser 200 para uma arte atualizada.");
    	assertEquals("null", updated.getString("startScheduleDate"), "A data de inicio deve ser nula nesse caso.");
    	assertEquals("null", updated.getString("endScheduleDate"), "A data de término deve ser nula nesse caso.");
    }
    
    @Test
    @DisplayName("Teste de falha de atualização dos dados de uma arte registrada passando uma data inicial anterior ao dia atual")
    void testDoChangeArtwork05() throws Exception {
    	String jsonPayload = String.format("""
		{
    		"artName": "Art Updated",
    		"haveSchedule": true,
    		"startScheduleDate": "2023-11-01",
    		"endScheduleDate": "%s"
		}
    	""", LocalDate.now().plusDays(1).toString());
    	Res response = updateArtworkById(this.firstArt.getString("artId"), jsonPayload);
    	assertEquals(400, response.status, "O código de status deve ser 400 Bad Request para uma arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de falha de atualização dos dados de uma arte registrada passando uma a data término de prazo anterior a data inicial de prazo")
    void testDoChangeArtwork06() throws Exception {
    	String jsonPayload = String.format("""
		{
    		"artName": "Art Updated",
    		"haveSchedule": true,
    		"startScheduleDate": "%s",
    		"endScheduleDate": "2023-11-01"
		}
    	""", LocalDate.now().toString());
    	Res response = updateArtworkById(this.firstArt.getString("artId"), jsonPayload);
    	assertEquals(400, response.status, "O código de status deve ser 400 Bad Request para uma arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de sucesso da adição de uma imagem produto a uma arte registrada no sistema do arthub.")
    void testDoAddArtImageProduct01() throws Exception {
    	testGetArts();
    	changeArtStatus(this.firstArt.getString("artId"), "PROGRESS");
    	MultipartFile file = MultipartFileUtils.convertToMultipartfile(makeFileData("test.png"));
    	Res response = associateImageProdToArt(this.firstArt.getString("artId"), file.getBytes());
    	
    	assertEquals(200, response.status, "O código de status deve ser 200 para uma imagem de produto associada.");
    }
    
    @Test
    @DisplayName("Teste de falha da adição de uma imagem produto a uma arte registrada no sistema do arthub passando um arquivo html.")
    void testDoAddArtImageProduct02() throws Exception {
    	testGetArts();
    	changeArtStatus(this.firstArt.getString("artId"), "PROGRESS");
    	MultipartFile file = MultipartFileUtils.convertToMultipartfile(makeFileData("index.html"));
    	Res response = associateImageProdToArt(this.firstArt.getString("artId"), file.getBytes());
    	
    	assertEquals(400, response.status, "O código de status deve ser 400 para uma imagem de produto associada.");
    }
    
    @Test
    @DisplayName("Teste de falha da adição de uma imagem produto a uma arte registrada no sistema do arthub que esta em TODO.")
    void testDoAddArtImageProduct03() throws Exception {
    	testDoCreateAnArt01();
    	JSONObject art = this.array.getJSONObject(this.array.length() - 1);
    	testGetArts();
    	MultipartFile file = MultipartFileUtils.convertToMultipartfile(makeFileData("test.png"));
    	Res response = associateImageProdToArt(art.getString("artId"), file.getBytes());
    	
    	assertEquals(400, response.status, "O código de status deve ser 400 para uma imagem de produto associada.");
    }
    
    @Test
    @DisplayName("Teste de sucesso da mudança de visibilidade para não listada de uma arte registrada no sistema do arthub")
    void testDoChangeVisibility01() throws Exception {
    	int status = updateArtworkVisibility(this.firstArt.getString("artId"), "NOT_LISTED");
    	assertEquals(200, status, "O código de status deve ser 200 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de falha da mudança de visibilidade para público de uma arte registrada no sistema do arthub")
    void testDoChangeVisibility02() throws Exception {
    	testDoCreateAnArt01();
    	testGetArts();
    	JSONObject art = this.array.getJSONObject(this.array.length() - 1);
    	int status = updateArtworkVisibility(art.getString("artId"), "PUBLIC");
    	assertEquals(400, status, "O código de status deve ser 400 Bad Request para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de sucesso da mudança de visibilidade para público de uma arte registrada no sistema do arthub")
    void testDoChangeVisibility03() throws Exception {
      	JSONObject art = makeAnArt();
    	changeArtStatus(art.getString("artId"), "PROGRESS");
    	MultipartFile file = MultipartFileUtils.convertToMultipartfile(makeFileData("test.png"));
    	associateImageProdToArt(art.getString("artId"), file.getBytes());
    	int status = updateArtworkVisibility(art.getString("artId"), "PUBLIC");
    	assertEquals(200, status, "O código de status deve ser 200 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de sucesso da mudança de visibilidade para privado de uma arte registrada no sistema do arthub")
    void testDoChangeVisibility04() throws Exception {
    	JSONObject art = makeAnArt();
    	changeArtStatus(art.getString("artId"), "PROGRESS");
    	MultipartFile file = MultipartFileUtils.convertToMultipartfile(makeFileData("test.png"));
    	associateImageProdToArt(art.getString("artId"), file.getBytes());
    	updateArtworkVisibility(art.getString("artId"), "PUBLIC");
    	int status = updateArtworkVisibility(art.getString("artId"), "PRIVATE");
    	assertEquals(200, status, "O código de status deve ser 200 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de sucesso de mudança de status para em progresso")
    void testDoChangeArtworkStatus01() throws Exception {
      	JSONObject art = makeAnArt();
      	int status = changeArtStatus(art.getString("artId"), "PROGRESS");
      	assertEquals(200, status, "O código de status deve ser 200 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de sucesso de mudança de status para finalizada")
    void testDoChangeArtworkStatus02() throws Exception {
      	JSONObject art = makeAnArt();
      	changeArtStatus(art.getString("artId"), "PROGRESS");
      	int status = changeArtStatus(art.getString("artId"), "FINISHED");
      	assertEquals(200, status, "O código de status deve ser 200 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de sucesso de mudança de status para arquivada")
    void testDoChangeArtworkStatus03() throws Exception {
      	JSONObject art = makeAnArt();
      	changeArtStatus(art.getString("artId"), "PROGRESS");
      	int status = changeArtStatus(art.getString("artId"), "DRAWNER");
      	assertEquals(200, status, "O código de status deve ser 200 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de sucessp de mudança de status de em arquivada para em progresso")
    void testDoChangeArtworkStatus04() throws Exception {
      	JSONObject art = makeAnArt();
      	changeArtStatus(art.getString("artId"), "PROGRESS");
      	changeArtStatus(art.getString("artId"), "DRAWNER");
      	int status = changeArtStatus(art.getString("artId"), "PROGRESS");
      	assertEquals(200, status, "O código de status deve ser 200 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de sucessp de mudança de status de em finalizado para em progresso")
    void testDoChangeArtworkStatus05() throws Exception {
      	JSONObject art = makeAnArt();
      	changeArtStatus(art.getString("artId"), "PROGRESS");
      	changeArtStatus(art.getString("artId"), "FINISHED");
      	int status = changeArtStatus(art.getString("artId"), "PROGRESS");
      	assertEquals(200, status, "O código de status deve ser 200 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de falha de mudança de status de TODO para finalizada")
    void testDoChangeArtworkStatus06() throws Exception {
      	JSONObject art = makeAnArt();
      	int status = changeArtStatus(art.getString("artId"), "FINISHED");
      	assertEquals(400, status, "O código de status deve ser 400 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de falha de mudança de status de TODO para arquivada")
    void testDoChangeArtworkStatus07() throws Exception {
      	JSONObject art = makeAnArt();
      	int status = changeArtStatus(art.getString("artId"), "DRAWNER");
      	assertEquals(400, status, "O código de status deve ser 400 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de falha de mudança de status de arquivada para finalizada")
    void testDoChangeArtworkStatus08() throws Exception {
      	JSONObject art = makeAnArt();
      	changeArtStatus(art.getString("artId"), "PROGRESS");
      	changeArtStatus(art.getString("artId"), "DRAWNER");
      	int status = changeArtStatus(art.getString("artId"), "FINISHED");
      	assertEquals(400, status, "O código de status deve ser 400 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de falha de mudança de status de finalizada para arquivada")
    void testDoChangeArtworkStatus09() throws Exception {
      	JSONObject art = makeAnArt();
      	changeArtStatus(art.getString("artId"), "PROGRESS");
      	changeArtStatus(art.getString("artId"), "FINISHED");
      	int status = changeArtStatus(art.getString("artId"), "DRAWNER");
      	assertEquals(400, status, "O código de status deve ser 400 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de falha de mudança de status de finalizada para TODO")
    void testDoChangeArtworkStatus10() throws Exception {
      	JSONObject art = makeAnArt();
      	changeArtStatus(art.getString("artId"), "PROGRESS");
      	changeArtStatus(art.getString("artId"), "FINISHED");
      	int status = changeArtStatus(art.getString("artId"), "TODO");
      	assertEquals(400, status, "O código de status deve ser 400 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de falha de mudança de status de arquivada para TODO")
    void testDoChangeArtworkStatus11() throws Exception {
      	JSONObject art = makeAnArt();
      	changeArtStatus(art.getString("artId"), "PROGRESS");
      	changeArtStatus(art.getString("artId"), "DRAWNER");
      	int status = changeArtStatus(art.getString("artId"), "TODO");
      	assertEquals(400, status, "O código de status deve ser 400 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de falha de mudança de status de em progresso para TODO")
    void testDoChangeArtworkStatus12() throws Exception {
      	JSONObject art = makeAnArt();
      	changeArtStatus(art.getString("artId"), "PROGRESS");
      	int status = changeArtStatus(art.getString("artId"), "TODO");
      	assertEquals(400, status, "O código de status deve ser 400 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de sucesso para adição de imagens de referência em uma arte")
    void testDoAddArtImageRefs01() throws Exception {
    	JSONObject art = makeAnArt();
    	List<ArtImageReferencePayload> refs = new ArrayList<ArtImageReferencePayload>();
    	refs.add(makeImgRef(true));
    	refs.add(makeImgRef(false));
    	int status = addImagesRefsToArt(art.getString("artId"),refs, false);
    	assertEquals(200, status, "O código de status deve ser 200 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de falha para adição de imagens de referência em uma arte com uma arquivo html")
    void testDoAddArtImageRefs02() throws Exception {
    	JSONObject art = makeAnArt();
    	List<ArtImageReferencePayload> refs = new ArrayList<ArtImageReferencePayload>();
    	refs.add(makeImgRef(true, true));
    	int status = addImagesRefsToArt(art.getString("artId"),refs, false);
    	assertEquals(400, status, "O código de status deve ser 400 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de falha para adição de imagens de referência em uma arte com o link da imagem vazia")
    void testDoAddArtImageRefs03() throws Exception {
    	JSONObject art = makeAnArt();
    	List<ArtImageReferencePayload> refs = new ArrayList<ArtImageReferencePayload>();
    	refs.add(makeImgRef(false, true));
    	int status = addImagesRefsToArt(art.getString("artId"),refs, false);
    	assertEquals(400, status, "O código de status deve ser 400 para a arte atualizada.");
    }
    
    @Test
    @DisplayName("Teste de sucesso para atualização das imagens de referências de u")
    void testDoUpdateAllArtImageRefs01() throws Exception {
    	JSONObject art = makeAnArt();
    	List<ArtImageReferencePayload> refs = new ArrayList<ArtImageReferencePayload>();
    	refs.add(makeImgRef(true));
    	refs.add(makeImgRef(false));
    	addImagesRefsToArt(art.getString("artId"),refs, false);
    	JSONObject artUpdated = getArtById(UUID.fromString(art.getString("artId"))).obj.getJSONObject("data");
    	
    	List<ArtImageReferencePayload> payloadRefs = new ArrayList<>();
    	JSONArray jsonRefs = artUpdated.getJSONArray("imgRefs");
    	for(int i = 0; i < jsonRefs.length(); i++) {
    		ArtImageReferencePayload payload = new ArtImageReferencePayload();
    		ArtImageReferencePayload newData = makeImgRef(jsonRefs.getJSONObject(i).getBoolean("externalUpload"));
    		
    		payload.setRefId(UUID.fromString(jsonRefs.getJSONObject(i).getString("imgRefId")));
    		payload.setImageLink(newData.getImageLink());
    		payload.setFileData(newData.getFileData());
    		payload.setUploadType(newData.getUploadType());
    		payloadRefs.add(payload);
    	}
    	
    	int status = addImagesRefsToArt(art.getString("artId"),payloadRefs, true);
    	
    	assertEquals(200, status, "O status deve ser 200 para a arte atualizada");
    }
    
    /* Util Methods */

    ArtImageReferencePayload makeImgRef(boolean bFileData, boolean bUseNotValid) throws Exception { 
    	ArtImageReferencePayload payload = new ArtImageReferencePayload();
    	if(bFileData) {
    		payload.setUploadType(ArtImageReferenceUploadType.DEVICE_UPLOAD);
    		payload.setFileData(makeFileData(bUseNotValid ? "index.html" : "test.png"));
    	} else {
    		payload.setUploadType(ArtImageReferenceUploadType.PINTEREST_API);
    		payload.setImageLink(bUseNotValid ? "" : "https://i.pinimg.com/564x/31/dd/fb/31ddfb68fadc71358702c492c1e7e9c8.jpg");
    	}
    	return payload;
    }
    
    ArtImageReferencePayload makeImgRef(boolean bFileData) throws Exception { 
    	ArtImageReferencePayload payload = new ArtImageReferencePayload();
    	if(bFileData) {
    		payload.setUploadType(ArtImageReferenceUploadType.DEVICE_UPLOAD);
    		payload.setFileData(makeFileData("test.png"));
    	} else {
    		payload.setUploadType(ArtImageReferenceUploadType.PINTEREST_API);
    		payload.setImageLink("https://i.pinimg.com/564x/31/dd/fb/31ddfb68fadc71358702c492c1e7e9c8.jpg");
    	}
    	return payload;
    }
    
    JSONObject makeAnArt() throws Exception {
    	testDoCreateAnArt01();
    	testGetArts();
    	return this.array.getJSONObject(this.array.length() - 1);
    }
    
    int addImagesRefsToArt(String id, List<ArtImageReferencePayload> refs, boolean bUpdate) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("{").append("\n");
    	sb.append("\t").append("\"artImageRef\": [").append("\n");
    	for (int i = 0; i < refs.size(); i++) {
    	    ArtImageReferencePayload ref = refs.get(i);
    	    sb.append("\t\t").append("{").append("\n");
    	    if(bUpdate)
    	    	 sb.append("\t\t\t").append("\"refId\": \"").append(ref.getRefId()).append("\",").append("\n");
    	    
    	    if (ref.getUploadType() == ArtImageReferenceUploadType.DEVICE_UPLOAD) {
    	        sb.append("\t\t\t").append("\"uploadType\": \"DEVICE_UPLOAD\",").append("\n");
    	        sb.append("\t\t\t").append("\"fileData\": {").append("\n");
    	        sb.append("\t\t\t\t").append("\"base64\": ").append("\"").append(ref.getFileData().getBase64()).append("\",").append("\n");
    	        sb.append("\t\t\t\t").append("\"fileName\": ").append("\"").append(ref.getFileData().getFileName()).append("\",").append("\n");
    	        sb.append("\t\t\t\t").append("\"contentType\": ").append("\"").append(ref.getFileData().getContentType()).append("\"\n");
    	        sb.append("\t\t\t").append("}").append("\n");

    	    } else {
    	        sb.append("\t\t\t").append("\"uploadType\": \"PINTEREST_API\",").append("\n");
    	        sb.append("\t\t\t").append("\"fileData\": {").append("\n");
    	        sb.append("\t\t\t\t").append("\"base64\": null,").append("\n");
    	        sb.append("\t\t\t\t").append("\"fileName\": null,").append("\n");
    	        sb.append("\t\t\t\t").append("\"contentType\": null").append("\n");
    	        sb.append("\t\t\t").append("},").append("\n");
    	        sb.append("\t\t\t").append("\"imageLink\": ").append("\"").append(ref.getImageLink()).append("\"\n");
    	    }

    	    sb.append("\t\t").append("}");
    	    if (i < refs.size() - 1) {
    	        sb.append(",");
    	    }
    	    sb.append("\n");
    	}
    	sb.append("\t").append("]").append("\n");
    	sb.append("}");

    	String body = sb.toString();
    	
    	
    	String authToken = "Bearer " + authUtil.getAuthToken();
        URL url = new URL("http://localhost:8080/art/v1/" + (bUpdate ? "imgReference/update" : "imgReference/add") + "?artId=" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", authToken);
        connection.setRequestMethod(bUpdate ? "PUT" : "POST");
        connection.setDoOutput(true);
        
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return connection.getResponseCode();
    }
    
    int changeArtStatus(String id, String status) throws Exception {
         String authToken = "Bearer " + authUtil.getAuthToken();
         String url = "http://localhost:8080/art/v1/changeStatus?artId=" + id + "&status=" + status;

         try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
             HttpPatch patchRequest = new HttpPatch(url);
             patchRequest.setHeader("Authorization", authToken);
             
             try (CloseableHttpResponse response = httpClient.execute(patchRequest)) {
                 int statusCode = response.getCode();
                 return statusCode;
             }
         }
    }
    
    Res associateImageProdToArt(String id, byte[] imageBytes) throws Exception {
        String authToken = "Bearer " + authUtil.getAuthToken();
        URL url = new URL("http://localhost:8080/art/v1/imgProduct/add?artId=" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", authToken);
        
        // Define boundary para multipart/form-data
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream();
             DataOutputStream dos = new DataOutputStream(os)) {

            // Escreve a parte do arquivo
            dos.writeBytes("--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"image.jpg\"\r\n");
            dos.writeBytes("Content-Type: image/jpeg\r\n\r\n"); // Ajuste o tipo de mídia conforme o tipo de imagem
            dos.write(imageBytes);
            dos.writeBytes("\r\n");

            // Finaliza o body do multipart
            dos.writeBytes("--" + boundary + "--\r\n");
            dos.flush();
        }

        // Lê o código de resposta
        int status = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        // Lê a resposta
        try (InputStream responseStream = (status == 200 ? connection.getInputStream() : connection.getErrorStream());
             BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, "utf-8"))) {

            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return new Res(status, new JSONObject(response.toString()));
    }
    
    public int updateArtworkVisibility(String id, String visibility) throws Exception {
        String authToken = "Bearer " + authUtil.getAuthToken();
        String url = "http://localhost:8080/art/v1/changeVisibility?artId=" + id + "&visibility=" + visibility;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPatch patchRequest = new HttpPatch(url);
            patchRequest.setHeader("Authorization", authToken);
            
            try (CloseableHttpResponse response = httpClient.execute(patchRequest)) {
                int statusCode = response.getCode();
                return statusCode;
            }
        }
    }
    
    Res updateArtworkById(String id, String payloadJson) throws Exception {
    	 String authToken = "Bearer " + authUtil.getAuthToken();
         URL url = new URL("http://localhost:8080/art/v1/update?artId=" + id);
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("PUT");
         connection.setRequestProperty("Content-Type", "application/json");
         connection.setRequestProperty("Authorization", authToken);
         connection.setDoOutput(true);
         
         try (OutputStream os = connection.getOutputStream()) {
             byte[] input = payloadJson.getBytes("utf-8");
             os.write(input, 0, input.length);
         }

         // Lê a resposta
         int status = connection.getResponseCode();

         StringBuilder response = new StringBuilder();
         
         // Escolhe o stream correto com base no status
         try (InputStream responseStream = (status == 200 ? connection.getInputStream() : connection.getErrorStream());
              BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, "utf-8"))) {
             
             String responseLine;
             while ((responseLine = br.readLine()) != null) {
                 response.append(responseLine.trim());
             }
         }

         return new Res(status,new JSONObject(response.toString()));
    }
    
    int deleteAnArt(String id) throws Exception {
    	String authToken = "Bearer " + authUtil.getAuthToken();
        URL url = new URL("http://localhost:8080/art/v1/fullDelete?artId=" + id.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Authorization", authToken);
        return connection.getResponseCode();
    }
    
    Res getArtById(UUID id) throws Exception {
        String authToken = "Bearer " + authUtil.getAuthToken();
        URL url = new URL("http://localhost:8080/art/v1/details/" + id.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", authToken);
        
        int status = connection.getResponseCode();

        StringBuilder response = new StringBuilder();
        
        // Escolhe o stream correto com base no status
        try (InputStream responseStream = (status == 200 ? connection.getInputStream() : connection.getErrorStream());
             BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, "utf-8"))) {
            
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return new Res(status,new JSONObject(response.toString()));
    }

    
    void testGetArts() throws Exception {
    	testDoCreateAnArt01(); // garante que a lista nao esteja vazia
		String authToken = "Bearer " + authUtil.getAuthToken();
		URL url = new URL("http://localhost:8080/art/v1/arts");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Authorization", authToken);
		 
		int status = connection.getResponseCode();
		assertEquals(200, status, "O código de status deve ser 200 OK");
		
		StringBuilder response = new StringBuilder();
		try (InputStream responseStream = connection.getInputStream();
		     BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, "utf-8"))) {
		     
		     String responseLine;
		     while ((responseLine = br.readLine()) != null) {
		         response.append(responseLine.trim());
		     }
		 }
		
		JSONObject jsonResponse = new JSONObject(response.toString());
		 
		assertEquals(false, jsonResponse.getBoolean("hasErrors"), "A resposta não deve conter erros");
		JSONArray artsArray = jsonResponse.getJSONArray("data");
		assertFalse(artsArray.length() == 0, "A lista de artes não deve estar vazia");
		
		JSONObject firstArt = artsArray.getJSONObject(0);
		this.firstArt = firstArt;
		this.array = artsArray;
		sysout("Primeira arte: " + firstArt.toString());
		
		assertNotNull(firstArt.getString("artId"), "A arte deve conter um ID");
		assertNotNull(firstArt.getString("artName"), "A arte deve conter um nome");
    }
    
    void testCreateArt(String payloadJson) throws Exception { 
    	 String authToken = "Bearer " + authUtil.getAuthToken();
         URL url = new URL("http://localhost:8080/art/v1/create");
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setRequestProperty("Content-Type", "application/json");
         connection.setRequestProperty("Authorization", authToken);
         connection.setRequestMethod("POST");
         connection.setDoOutput(true);
         
         try (OutputStream os = connection.getOutputStream()) {
             byte[] input = payloadJson.getBytes("utf-8");
             os.write(input, 0, input.length);
         }

         // Lê a resposta
         int status = connection.getResponseCode();
         
         // Capture a resposta em caso de erro
         StringBuilder response = new StringBuilder();
         try (InputStream responseStream = (status >= 200 && status < 300) 
                 ? connection.getInputStream() 
                 : connection.getErrorStream(); // Use o fluxo de erro se o status não for sucesso
              BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, "utf-8"))) {
             
             String responseLine;
             while ((responseLine = br.readLine()) != null) {
                 response.append(responseLine.trim());
             }
         }

         // Verifica o código de status
         if (status == 201) {
        	 sysout("Resposta da API: " + response.toString());
             assertEquals(true, response.toString().contains("\"status\":201"), "A resposta deve conter status 201");
             assertEquals(true, response.toString().contains("\"hasErrors\":false"), "A resposta deve indicar que não há erros");
         } else {
             // Caso haja um erro, imprime a resposta para debug
        	 syserr("Erro no teste 'criar arte'. Código de status: " + status);
        	 syserr("Resposta da API: " + response.toString());
             fail("A criação da arte falhou com código de status " + status);
         }
    }
    
    void testCreateArt(String payloadJson, int expectedStatus, boolean isError) throws Exception { 
        String authToken = "Bearer " + authUtil.getAuthToken();
        // Configura a URL e abre uma conexão
        URL url = new URL("http://localhost:8080/art/v1/create");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", authToken);
        connection.setDoOutput(true);

        // Envia o payload JSON
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payloadJson.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Lê a resposta
        int status = connection.getResponseCode();
        
        // Capture a resposta em caso de erro
        StringBuilder response = new StringBuilder();
        try (InputStream responseStream = (status >= 200 && status < 300) 
                ? connection.getInputStream() 
                : connection.getErrorStream(); // Use o fluxo de erro se o status não for sucesso
             BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, "utf-8"))) {
            
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        // Verifica o código de status
        if (status == expectedStatus) {
        	sysout("Resposta da API: " + response.toString());
            assertEquals(true, response.toString().contains("\"status\":" + expectedStatus), "A resposta deve conter status " + expectedStatus);
            assertEquals(true, response.toString().contains("\"hasErrors\":" + isError), 
                         isError ? "A resposta deve indicar que houve erros" : "A resposta deve indicar que não há erros");
        } else {
            // Caso haja um erro, imprime a resposta para debug
        	syserr("Erro no teste 'criar arte'. Código de status: " + status);
            syserr("Resposta da API: " + response.toString());
            fail("O teste 'criar arte' falhou. Esperado: " + expectedStatus + ", recebido: " + status);
        }
    }
    
    FileData makeFileData(String fileName) throws Exception{
    	return MultipartFileUtils.getFileDataFromResources(fileName);
    }
    
    void sysout(Object msg) {
    	if(bLog)
    		System.out.println(msg);
    }
    
    void syserr(Object msg) {
    	if(bLog)
    		System.err.println(msg);
    }
}

class Res {
	int status;
	JSONObject obj;
	
	public Res(int status, JSONObject obj) {
		this.status = status;
		this.obj = obj;
	}
}
