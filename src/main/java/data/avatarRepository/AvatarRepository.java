package data.avatarRepository;

import domain.Avatar;

import java.util.List;

public interface AvatarRepository {
    List<Avatar> getAllAvatarsFromUser(int userID);
    Avatar getAvatar(int avatarID);

    Avatar getAvatarID(String name);

    void changeAvatar(int avatarID, int userID);

}
