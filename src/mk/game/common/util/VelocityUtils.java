package mk.game.common.util;

import java.io.StringWriter;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class VelocityUtils
{
	private VelocityEngine velocityEngine;
	private Properties properties;
	private static final Logger logger = Logger.getLogger(VelocityUtils.class);

	public Template getTemplate(String templateName)
	{
		try
		{
			return velocityEngine.getTemplate(templateName);
		}
		catch (ResourceNotFoundException e)
		{
			logger.warn(e);
		}
		catch (ParseErrorException e)
		{
			logger.warn(e);
		}
		catch (Exception e)
		{
			logger.warn(e);
		}
		return null;
	}

	public String mergeWithTemplate(VelocityContext ctx, Template template)
	{
		StringWriter writer = new StringWriter();
		try
		{
			template.merge(ctx, writer);
		}
		catch (ResourceNotFoundException e)
		{
			logger.warn(e);
		}
		catch (ParseErrorException e)
		{
			logger.warn(e);
		}
		catch (MethodInvocationException e)
		{
			logger.warn(e);
		}
		catch (Exception e)
		{
			logger.warn(e);
		}
		return writer.toString();
	}

	public void init()
	{
		try
		{
			velocityEngine.init(properties);
		}
		catch (Exception e)
		{
			logger.warn(e);
		}
	}

	public void setVelocityEngine(VelocityEngine ve)
	{
		this.velocityEngine = ve;
	}

	public void setProperties(Properties properties)
	{
		this.properties = properties;
	}
}
