package br.com.arthub.ah_rest_art.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClearArtImageRefsPayload {
	private boolean clearAll;
	private List<UUID> refsId;
}
