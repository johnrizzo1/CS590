{ pkgs, lib, config, inputs, ... }:

{
  packages = with pkgs; [ 
    git
    openjdk11
    boost
  ];
  languages.python.enable = true;
  languages.python.venv.enable = true;
  languages.python.uv.enable = true;
  languages.java.enable = true;
  languages.cplusplus.enable = true;
  enterShell = ''
    git --version
    java --version
    python --version
  '';
}
