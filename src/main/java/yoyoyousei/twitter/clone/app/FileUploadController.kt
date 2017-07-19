package yoyoyousei.twitter.clone.app

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import yoyoyousei.twitter.clone.domain.service.UserIdNotFoundException
import yoyoyousei.twitter.clone.domain.service.UserService
import yoyoyousei.twitter.clone.domain.service.upload.StorageFileNotFoundException
import yoyoyousei.twitter.clone.domain.service.upload.StorageService
import yoyoyousei.twitter.clone.util.Util
import java.security.Principal
import java.util.*

/**
 * Created by s-sumi on 2017/03/06.
 */

//アップロードされたファイルをrootLocation以下に格納してファイル名を問い合わされたら提供する
@Controller
@RequestMapping("files")
class FileUploadController @Autowired
constructor(private val userService: UserService, private val storageService: StorageService) {
    //for debug
    /*@GetMapping("/")
    public String listUploadedFiles(Model model)throws IOException{
        model.addAttribute("files",storageService
            .loadAll().map(path-> MvcUriComponentsBuilder
                                    .fromMethodName(FileUploadController.class,"servFile",path.getFileName().toString()).build().toString())
            .collect(Collectors.toList()));
        return "uploadForm";
    }*/

    @GetMapping("/icon/{filename:.+}")
    @ResponseBody
    fun serveFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = storageService.loadAsResource(filename)
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.filename + "\"")
                .body(file)
    }

    @PostMapping("/icon/upload")
    fun handleFileUpload(principal: Principal, @RequestParam("icon") file: MultipartFile,
                         redirectAttributes: RedirectAttributes): String {

        val res = file.originalFilename.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val suffix = res[res.size - 1]
        if (!isImageExtension(suffix)) {
            val errors = HashSet<String>()
            errors.add("only image files can be uploaded.")
            redirectAttributes.addFlashAttribute("errors", errors)
            return "redirect:/update"
        }

        val filename = storageService.store(file)

        val user = Util.getLoginuserFromPrincipal(principal)
        val path = storageService.load(filename)

        log!!.info("path.filename.tostring: " + path.fileName.toString())
        user.iconPath = getPathStrFromFilename(path.fileName.toString())

        try {
            userService.update(user)
        } catch (e: UserIdNotFoundException) {
            val errors = HashSet<String>()
            errors.add(e.message!!)
            redirectAttributes.addFlashAttribute("errors", errors)
            return "redirect:/update"
        } catch (e: Exception) {
            val errors = HashSet<String>()
            errors.add("unexpected error occured. try again.")
            log!!.info(e.message)
            redirectAttributes.addFlashAttribute("errors", errors)
            return "redirect:/update"
        }

        Util.updateAuthenticate(principal, user)

        return "redirect:/"
    }

    fun getPathStrFromFilename(filename: String): String {
        return MvcUriComponentsBuilder.fromMethodName(FileUploadController::class.java, "serveFile", filename).build().toString()
    }

    fun isImageExtension(extension: String): Boolean {
        for (s in Util.imageExtensions) {
            if (s == extension) {
                return true
            }
        }
        return false
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(exc: StorageFileNotFoundException): ResponseEntity<*> {
        return ResponseEntity.notFound().build<Any>()
    }

    companion object {
        val log: Logger? = LoggerFactory.getLogger(FileUploadController::class.java)
    }

}
