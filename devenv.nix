{ pkgs, lib, config, inputs, ... }:

{
  packages = [ pkgs.git pkgs.openjdk11 ];
  languages.python.enable = true;
  languages.python.venv.enable = true;
  languages.python.uv.enable = true;
  languages.java.enable = true;
  enterShell = ''
    git --version
    java --version
    python --version
  '';
}
