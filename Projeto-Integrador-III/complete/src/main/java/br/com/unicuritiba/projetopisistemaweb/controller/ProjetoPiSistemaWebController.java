package br.com.unicuritiba.projetopisistemaweb.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.unicuritiba.projetopisistemaweb.localstorage.StorageFileNotFoundException;
import br.com.unicuritiba.projetopisistemaweb.localstorage.StorageService;
import br.com.unicuritiba.projetopisistemaweb.model.Anuncio;
import br.com.unicuritiba.projetopisistemaweb.repository.AnuncioRepository;

@Controller
public class ProjetoPiSistemaWebController {

	private final StorageService storageService;
	
	
	@Autowired
	public ProjetoPiSistemaWebController(StorageService storageService) {
		this.storageService = storageService;
	}

	@Autowired
	private AnuncioRepository repositorio;

	// Visualização Admin
	@RequestMapping(value = "/anuncios", method = RequestMethod.GET)
	public ModelAndView anunciosAdmin(Model model, @ModelAttribute Anuncio anuncio) throws IOException {
		
		List<Anuncio> anuncios = repositorio.findAll();

		ModelAndView modelAndView = new ModelAndView("anuncios");
		modelAndView.addObject("anuncios", anuncios);
		
		
		model.addAttribute("files",
				storageService.loadAll().map(path -> MvcUriComponentsBuilder
						.fromMethodName(ProjetoPiSistemaWebController.class, "serveFile", path.getFileName().toString())
						.build().toUri().toString()).collect(Collectors.toList()));
		return modelAndView;
	}
	
	// Visualização Visitantes
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(Model model, @ModelAttribute Anuncio anuncio) throws IOException {
		
		List<Anuncio> anuncios = repositorio.findAll();

		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("anuncios", anuncios);
		
		
		model.addAttribute("files",
				storageService.loadAll().map(path -> MvcUriComponentsBuilder
						.fromMethodName(ProjetoPiSistemaWebController.class, "serveFile", path.getFileName().toString())
						.build().toUri().toString()).collect(Collectors.toList()));
		return modelAndView;
	}

	// Requisicao para tornar imagem publicamente disponivel
	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	// Requisição tipo POST para copiar imagem para o diretório "static/images" e
	// gravar dados no banco
	@PostMapping("/anuncios")
	public String handleFileUpload(@ModelAttribute Anuncio anuncio, @RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		
		
		anuncio.setImagem(file.getOriginalFilename());
		Date dataAtual = new Date();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		anuncio.setDataPublicacao(formatador.format(dataAtual));
		
		storageService.store(file);
		repositorio.save(anuncio);
		
		redirectAttributes.addFlashAttribute("mensagemSucessoPublicacao", "Seu anuncio foi publicado com sucesso!");
		return "redirect:/anuncios";
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

	// deletar anuncio
	@RequestMapping(value = "/excluir_anuncio", method = RequestMethod.GET)
	public String botaoDeletaAnuncio(@ModelAttribute Anuncio anuncio, @RequestParam(name = "id") Long id,
			RedirectAttributes redirectAttributes) {		

				repositorio.deleteById(id);
				redirectAttributes.addFlashAttribute("mensagemSucessoExclusao",
						"Seu anuncio foi excluido com sucesso!");			
		return "redirect:/anuncios";
	}

}
