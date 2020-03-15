package com.cex0.mobiai.controller.admin.api;

import com.cex0.mobiai.model.annotation.DisableOnCondition;
import com.cex0.mobiai.model.dto.post.OptionDTO;
import com.cex0.mobiai.model.dto.post.OptionSimpleDTO;
import com.cex0.mobiai.model.entity.Option;
import com.cex0.mobiai.model.params.OptionParam;
import com.cex0.mobiai.model.params.OptionQuery;
import com.cex0.mobiai.service.OptionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import static org.springframework.data.domain.Sort.Direction.DESC;

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
    @DisableOnCondition
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

    @GetMapping("list_view")
    @ApiOperation("Lists all options with list view")
    public Page<OptionSimpleDTO> listAllWithListView(@PageableDefault(sort = "updateTime", direction = DESC) Pageable pageable,
                                                    OptionQuery optionQuery) {
        return optionService.pageDtosBy(pageable, optionQuery);
    }

    @GetMapping("{id:\\d+}")
    @ApiOperation("Gets option detail by id")
    public OptionSimpleDTO getBy(@PathVariable("id") Integer id) {
        Option option = optionService.getById(id);
        return optionService.convertToDto(option);
    }

    @PostMapping
    @DisableOnCondition
    @ApiOperation("Creates option")
    public void createBy(@RequestBody @Valid OptionParam optionParam) {
        optionService.save(optionParam);
    }

    @PutMapping("{optionId:\\d+}")
    @DisableOnCondition
    @ApiOperation("Updates option")
    public void updateBy(@PathVariable("optionId") Integer optionId,
                         @RequestBody @Valid OptionParam optionParam) {
        optionService.update(optionId, optionParam);
    }

    @DeleteMapping("{optionId:\\d+}")
    @DisableOnCondition
    @ApiOperation("Deletes option")
    public void deletePermanently(@PathVariable("optionId") Integer optionId) {
        optionService.removePermanently(optionId);
    }

    @PostMapping("map_view/saving")
    @DisableOnCondition
    @ApiOperation("Saves options by option map")
    public void saveOptionsWithMapView(@RequestBody Map<String, Object> optionMap) {
        optionService.save(optionMap);
    }
}
