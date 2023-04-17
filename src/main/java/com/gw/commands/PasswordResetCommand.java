package com.gw.commands;

import java.io.Console;
import java.util.Arrays;

import com.gw.utils.BaseTool;

import org.springframework.stereotype.Component;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "resetpassword", description = "Reset password of local host")
@Component
public class PasswordResetCommand implements Runnable {

    @CommandLine.Option(names = {"-p", "--password"}, description = "New password")
    private String newPassword;

    public void run() {

        if (newPassword == null || newPassword.isEmpty()) {
            System.out.println("Error: New password value is missing.");
            return;
        }

        char[] passwordArray = newPassword.toCharArray();
        char[] secondpasswordArray = newPassword.toCharArray();

        // compare password arrays to confirm
        if(Arrays.equals(passwordArray, secondpasswordArray)){

            String originalpassword = new String(passwordArray);

            BaseTool bt = new BaseTool();

            bt.setLocalhostPassword(originalpassword, true);

            System.out.println("NOTE: Password updated.");

        }else{

            System.err.println("ERROR: The two entered passwords don't match.");

        }

    }
}
