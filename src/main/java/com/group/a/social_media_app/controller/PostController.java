import ch.qos.logback.core.model.Model;
import com.group.a.social_media_app.dto.PostDTO;
import com.group.a.social_media_app.entity.Post;
import com.group.a.social_media_app.service.CustomUserDetailsService;
import com.group.a.social_media_app.service.PostService;
import com.group.a.social_media_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("newPost") PostDTO postDTO,
                             BindingResult result,
                             @AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal principal,
                             RedirectAttributes redirect) {
        if (result.hasErrors()) {
            redirect.addFlashAttribute("error", "Post content is required");
            return "redirect:/home";
        }

        User user = userService.findByEmail(principal.getUsername()).orElse(null);
        if (user == null) return "redirect:/login";

        postService.createPost(postDTO, user);
        redirect.addFlashAttribute("success", "Post created successfully!");
        return "redirect:/home";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id,
                             @AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal principal,
                             RedirectAttributes redirect) {
        Optional<Post> postOpt = postService.findById(id);
        if (postOpt.isEmpty() || !postOpt.get().getUser().getEmail().equals(principal.getUsername())) {
            redirect.addFlashAttribute("error", "You can only delete your own posts.");
            return "redirect:/home";
        }

        postService.deletePost(id);
        redirect.addFlashAttribute("success", "Post deleted.");
        return "redirect:/home";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id,
                           @AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal principal,
                           Model model,
                           RedirectAttributes redirect) {
        Post post = postService.findById(id).orElse(null);
        if (post == null || !post.getUser().getEmail().equals(principal.getUsername())) {
            redirect.addFlashAttribute("error", "Unauthorized.");
            return "redirect:/home";
        }

        model.addAttribute("post", postService.convertToDTO(post));
        return "home/edit-post";
    }

    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable Long id,
                             @Valid @ModelAttribute("post") PostDTO postDTO,
                             BindingResult result,
                             @AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal principal,
                             RedirectAttributes redirect) {
        Post post = postService.findById(id).orElse(null);
        if (post == null || !post.getUser().getEmail().equals(principal.getUsername())) {
            redirect.addFlashAttribute("error", "Unauthorized.");
            return "redirect:/home";
        }

        if (result.hasErrors()) {
            redirect.addFlashAttribute("error", "Post update failed.");
            return "redirect:/home";
        }

        post.setContent(postDTO.getContent());
        postService.updatePost(post);
        redirect.addFlashAttribute("success", "Post updated.");
        return "redirect:/home";
    }
}