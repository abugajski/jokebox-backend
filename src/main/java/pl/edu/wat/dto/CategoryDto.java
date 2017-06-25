package pl.edu.wat.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.edu.wat.model.Category;

import java.io.Serializable;

/**
 * Created by Hubert on 25.06.2017.
 */
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto implements Serializable {

    int id;
    String name;

    public CategoryDto(Category category){
        this.id = category.getId();
        this.name = category.getName();
    }

}
