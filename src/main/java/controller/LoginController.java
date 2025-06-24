package controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dto.LoginDTO;
import security.JWTUtil;
import service.LoginService;

@RestController
public class LoginController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	
	@PostMapping("/login")
	public ResponseEntity<Object> authenticatUser(@RequestBody LoginDTO loginDTO){
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
			
			UserDetails userDetails=loginService.loadUserByUsername(loginDTO.getEmail());
			
			String jwt=jwtUtil.generateToken(username);
			
			Map<String, Object> response= new HashMap<>();
			response.put("jwt", jwt);
			response.put("status", HttpStatus.OK.value());
			
			
			return ResponseEntity.ok(response);
		} 
		
		
		catch (BadCredentialsException e) {
			// TODO: handle exception
			return new ResponseEntity<>("Invalid credentials", HttpStatus.BAD_REQUEST);
		}
	}

}
