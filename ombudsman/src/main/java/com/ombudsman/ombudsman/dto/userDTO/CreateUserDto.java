public record CreateUserDto(

        String email,
        String password,
        RoleName role

) {
}