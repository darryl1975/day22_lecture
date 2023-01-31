package sg.edu.nus.iss.day22_lecture.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private Integer id;

    private String roomType;

    private Integer price;
}
