package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.security;

import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.auth.AuthRequest;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.auth.AuthResponse; // Assumindo que este é o DTO para retornar token e userId
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.User; // Importa sua entidade User
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository.UserRepository; // Adicionado para buscar User por email e pegar o ID

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails; // Para o tipo retornado por authentication.getPrincipal()
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Para o caso de usuário não encontrado após autenticação
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException; // Importar para lançar 401

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários (login)")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    // ANTES: @Autowired private TokenService tokenService;
    @Autowired // AGORA: Injeta JwtService
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired // Adicionado: Precisamos do UserRepository para buscar o User e pegar o ID
    private UserRepository userRepository;

    // Endpoint de login
    @PostMapping("/login")
    @Operation(summary = "Autentica um usuário e retorna um token JWT",
            description = "Recebe email e senha de um usuário, autentica e retorna um JWT para acesso futuro.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida, token JWT retornado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
            })
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest authRequest) {
        try {
            // Cria um objeto de autenticação com o email e senha fornecidos
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());

            // Tenta autenticar o usuário. Se as credenciais estiverem incorretas, lança uma AuthenticationException.
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Obtém o objeto UserDetails (que é o User que você carregou no AuthService)
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Precisamos do ID do usuário para o token e para retornar ao front-end.
            // O UserDetails padrão do Spring Security não tem o ID diretamente.
            // Então, buscamos a entidade User completa pelo email.
            User userEntity = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado após autenticação."));
            Long userId = userEntity.getId(); // Assumindo que sua entidade User tem um método getId()

            // Agora, gera o token JWT usando o JwtService
            // Passamos um mapa de claims extras (aqui, o userId) e o userDetails
            String token = jwtService.generateToken(java.util.Collections.singletonMap("userId", userId), userDetails);

            // Retorna o token e o ID do usuário na resposta AuthResponse
            return ResponseEntity.ok(new AuthResponse(token, userId));

        } catch (Exception e) {
            // Lidar com erros de autenticação (credenciais inválidas, etc.)
            // Captura AuthenticationException e outras, e retorna 401
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas: " + e.getMessage());
        }
    }

    // Endpoint para registrar um novo usuário (sem autenticação ainda neste ponto)
    // ATENÇÃO: Seu comentário indica que este endpoint está incompleto.
    // Para que ele funcione corretamente, você precisará injetar um UserRepository ou um UserService
    // para realmente salvar o newUser no banco de dados.
    // Por exemplo:
    // @Autowired
    // private UserRepository userRepository;
    @PostMapping("/register")
    @Operation(summary = "Registra um novo usuário na aplicação",
            description = "Cria um novo usuário com o email e senha fornecidos. A senha é criptografada antes de salvar.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados de registro inválidos")
            })
    public ResponseEntity<User> register(@RequestBody @Valid AuthRequest authRequest) {
        // Criar um novo objeto User a partir do AuthRequest
        User newUser = new User();
        // CUIDADO: Definir o nome como o email pode não ser o ideal. Considere um campo 'name' no AuthRequest.
        newUser.setName(authRequest.getEmail());
        newUser.setEmail(authRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(authRequest.getPassword())); // Criptografa a senha!

        // *** IMPORTANTE: AQUI É ONDE VOCÊ PRECISA SALVAR O USUÁRIO NO BANCO DE DADOS ***
        // Exemplo:
        // User savedUser = userRepository.save(newUser); // Descomente e use seu userRepository
        // return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

        // Por enquanto, apenas para compilar e demonstrar a estrutura (não salva no DB):
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}