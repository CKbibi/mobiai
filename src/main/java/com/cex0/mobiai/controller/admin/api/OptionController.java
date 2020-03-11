package com.cex0.mobiai.controller.admin.api;

import com.cex0.mobiai.model.dto.post.OptionDTO;
import com.cex0.mobiai.model.params.OptionParam;
import com.cex0.mobiai.service.OptionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Option Controller
 *
 * @author Cex0
 * @date 2020/02/28
 */
@RestController
@RequestMapping("/api/admin/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @ApiOperation("Lists options")
    public List<OptionDTO> listAll() {
        return optionService.listDtos();
    }

    @PostMapping("saving")
    @ApiOperation("Saves options")
    public void saveOptions(@Valid @RequestBody List<OptionParam> optionParams) {
        optionService.save(optionParams);
    }

    @GetMapping("map_view")
    @ApiOperation("Lists all options with map view")
    public Map<String, Object> listAllWithMapView(@RequestParam(value = "key[]", required = false) List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return optionService.listOptions();
        }

        return optionService.listOptions(keys);
    }

    @PostMapping("map_view/saving")
    @ApiOperation("Saves options by option map")
    public void saveOptionsWithMapView(@RequestBody Map<String, Object> optionMap) {
        optionService.save(optionMap);
    }
}
