{
  description = "PHD portal project";

  inputs = {
    devenv-root = {
      url = "file+file:///dev/null";
      flake = false;
    };
    flake-parts.url = "github:hercules-ci/flake-parts";
    nixpkgs.url = "github:cachix/devenv-nixpkgs/rolling";
    devenv.url = "github:cachix/devenv";
  };

  nixConfig = {
    extra-trusted-public-keys = "devenv.cachix.org-1:w1cLUi8dv3hnoSPGAuibQv+f9TZLr6cv/Hm9XgU50cw=";
    extra-substituters = "https://devenv.cachix.org";
  };

  outputs = inputs @ {
    nixpkgs,
    flake-parts,
    devenv-root,
    ...
  }:
    flake-parts.lib.mkFlake {inherit inputs;} {
      imports = [
        inputs.devenv.flakeModule
      ];
      systems = nixpkgs.lib.systems.flakeExposed;

      perSystem = {
        config,
        self',
        inputs',
        pkgs,
        lib,
        system,
        ...
      }: {
        _module.args.pkgs = import inputs.nixpkgs {
          inherit system;
          config.allowUnfree = true;
        };

        devenv.shells.default = {
          name = "Quarkus project";

          languages.java = {
            enable = true;
            jdk.package = pkgs.jdk;
            gradle.enable = true;
          };

          # NOTE: use devenv up to run the service
          services = {
            postgres = {
              enable = false;
              listen_addresses = "127.0.0.1";
              port = 5432;
              initialDatabases = [
                {
                  name = "phd";
                  user = "phd";
                  pass = "phd";
                }
              ];
            };
            elasticsearch = {
              enable = true;
              cluster_name = "elasicsearch";
              port = 9200;
            };
          };

          git-hooks.hooks = {
            # Common
            commitizen.enable = true;

            actionlint = {
              enable = false;
              excludes = ["docker-publish.yaml"];
            };
            checkmake.enable = true;
          };

          # NOTE: aws-vauld add tu-varna-phd
          aws-vault = {
            enable = true;
            profile = "tu-varna-phd";
            awscliWrapper.enable = true;
          };
          env = {
            AWS_DEFAULT_PROFILE = "tu-varna-phd";
            AWS_DEFAULT_REGION = "eu-west-1";
          };

          devenv.root = let
            devenvRootFileContent = builtins.readFile devenv-root.outPath;
          in
            pkgs.lib.mkIf (devenvRootFileContent != "") devenvRootFileContent;

          packages = with pkgs; [
            quarkus
            aws-vault
          ];
        };
      };
    };
}
