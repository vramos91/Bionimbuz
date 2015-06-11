    package br.unb.cic.bionimbus.client.shell.commands;

import br.unb.cic.bionimbus.avro.gen.BioProto;
import br.unb.cic.bionimbus.client.shell.Command;
import br.unb.cic.bionimbus.client.shell.SimpleShell;
import br.unb.cic.bionimbus.services.security.PDP;
import com.google.common.base.Joiner;

public class ListFiles implements Command {

    public static final String NAME = "files";
    private final SimpleShell shell;

    public ListFiles(SimpleShell shell) {
        this.shell = shell;
    }

    @Override
    public String execute(String... params) throws Exception {

        if (!shell.isConnected()) {
            throw new IllegalStateException("This command should be used with an active connection!");
        }
        BioProto proxy = shell.getProxy();
        PDP arquivos = new PDP();
       
       return Joiner.on("\n").join(arquivos.mostraArquivos());
    }

    @Override
    public String usage() {
        return NAME;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void setOriginalParamLine(String param) {
    }
}
