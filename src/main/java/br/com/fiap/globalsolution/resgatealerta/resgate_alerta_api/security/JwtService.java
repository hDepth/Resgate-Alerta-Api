package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value; // Importar Value
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // A chave secreta será lida do application.properties ou application.yml
    // Use um valor padrão LONGO como fallback, mas a boa prática é tê-lo no .properties
    @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String SECRET_KEY; // Renomeado para consistência com o que você tinha

    // Tempo de expiração do token (em milissegundos)
    // 1 hora = 60 * 60 * 1000 = 3600000
    @Value("${jwt.expiration.millis:3600000}")
    private long EXPIRATION_TIME; // Usar long para corresponder ao tipo do @Value

    // Métodos para extrair claims
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Método para obter a chave de assinatura decodificada
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Método para gerar o token sem claims extras (apenas o username)
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Método principal para gerar o token com claims extras
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Adicione o userId ao token aqui, se você precisar dele no lado do cliente
        // Exemplo: se userDetails for uma implementação sua que tem getUserId()
        // if (userDetails instanceof CustomUserDetails) {
        //    extraClaims.put("userId", ((CustomUserDetails) userDetails).getUserId());
        // }

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Método para validar o token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // NOVO MÉTODO (ou adapte um existente) para extrair o userId do token
    // Se você estiver colocando userId no token como um claim customizado
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }
}