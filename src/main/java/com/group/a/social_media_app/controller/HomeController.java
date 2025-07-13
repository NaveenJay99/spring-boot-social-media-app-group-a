import ch.qos.logback.core.model.Model;
import com.group.a.social_media_app.dto.PostDTO;
import com.group.a.social_media_app.entity.User;
import com.group.a.social_media_app.service.CustomUserDetailsService;
import com.group.a.social_media_app.service.PostService;
import com.group.a.social_media_app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal principal,
                       Model model) {
        String email = principal.getUsername();
        User user = userService.findByEmail(email).orElse(null);
        if (user == null) return "redirect:/login?error=true";

        List<PostDTO> posts = postService.convertToDTOs(postService.getPostsByUser(user));
        model.addAttribute("currentUser", user);
        model.addAttribute("posts", posts);
        model.addAttribute("postCount", postService.getPostCountByUser(user));
        model.addAttribute("newPost", new PostDTO());
        return "home/feed";
    }
}