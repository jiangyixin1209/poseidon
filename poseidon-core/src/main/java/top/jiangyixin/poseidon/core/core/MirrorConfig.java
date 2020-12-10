package top.jiangyixin.poseidon.core.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.poseidon.core.exception.PoseidonException;
import top.jiangyixin.poseidon.core.util.StringUtils;

/**
 * @author jiangyixin
 */
public class MirrorConfig {
	private static final Logger logger = LoggerFactory.getLogger(MirrorConfig.class);
	private static String mirrorFile;

	public static void init(String mirrorFile) {
		if (StringUtils.isEmpty(mirrorFile)) {
			throw new PoseidonException("MirrorFile can not be empty");
		}
		MirrorConfig.mirrorFile = mirrorFile;
	}
}
