package cinema.controller;

import cinema.dto.ShoppingCartResponseDto;
import cinema.model.ShoppingCart;
import cinema.service.MovieSessionService;
import cinema.service.ShoppingCartService;
import cinema.service.UserService;
import cinema.service.mapper.ShoppingCartMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final MovieSessionService movieSessionService;
    private final ShoppingCartMapper shoppingCartMapper;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService,
                                  MovieSessionService movieSessionService,
                                  ShoppingCartMapper shoppingCartMapper) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.movieSessionService = movieSessionService;
        this.shoppingCartMapper = shoppingCartMapper;
    }

    @PostMapping("/movie-sessions")
    public void addSession(Authentication authentication, @RequestParam Long sessionId) {
        String email = authentication.getName();
        shoppingCartService.addSession(movieSessionService.get(sessionId),
                userService.findByEmail(email).get());
    }

    @GetMapping("/by-user")
    public ShoppingCartResponseDto getByUser(Authentication authentication) {
        String email = authentication.getName();
        ShoppingCart byUser = shoppingCartService.getByUser(userService.findByEmail(email).get());
        return shoppingCartMapper.cartToDto(byUser);
    }
}
