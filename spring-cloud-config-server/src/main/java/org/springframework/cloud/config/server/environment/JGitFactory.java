package org.springframework.cloud.config.server.environment;

public static class JGitFactory {

	private final boolean cloneSubmodules;

	public JGitFactory() {
		this(false);
	}

	public JGitFactory(boolean cloneSubmodules) {
		this.cloneSubmodules = cloneSubmodules;
	}

	public Git getGitByOpen(File file) throws IOException {
		Git git = Git.open(file);
		return git;
	}

	public CloneCommand getCloneCommandByCloneRepository() {
		CloneCommand command = Git.cloneRepository().setCloneSubmodules(cloneSubmodules);
		return command;
	}

}
